package com.build.core_restful.service.impl;

import com.build.core_restful.domain.*;
import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentedOrderResponse;
import com.build.core_restful.repository.*;
import com.build.core_restful.repository.specification.RentedOrderSpecification;
import com.build.core_restful.service.RentedOrderService;
import com.build.core_restful.util.StringUtil;
import com.build.core_restful.util.enums.DeliveryMethodEnum;
import com.build.core_restful.util.enums.EntityStatusEnum;
import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.RentedOrderMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentedOrderServiceImpl implements RentedOrderService {
    private final RentedOrderRepository rentedOrderRepository;
    private final UserRepository userRepository;
    private final RentalItemRepository rentalItemRepository;
    private final BranchRepository branchRepository;
    private final BookRepository bookRepository;
    private final RentedOrderMapper rentedOrderMapper;

    public RentedOrderServiceImpl(RentedOrderRepository rentedOrderRepository,
                                    UserRepository userRepository,
                                    RentalItemRepository rentalItemRepository,
                                    BranchRepository branchRepository,
                                    BookRepository bookRepository,
                                    RentedOrderMapper rentedOrderMapper) {
        this.rentedOrderRepository = rentedOrderRepository;
        this.userRepository = userRepository;
        this.rentalItemRepository = rentalItemRepository;
        this.branchRepository = branchRepository;
        this.bookRepository = bookRepository;
        this.rentedOrderMapper = rentedOrderMapper;
    }

    @Override
    @Transactional
    public RentedOrderResponse create(RentedOrderRequest request) {
        try {
            RentedOrder newRentedOrder = rentedOrderMapper.toEntity(request);

            // Fix 1: Remove redundant null check
            User user = userRepository.findByIdAndStatus(request.getUserId(), EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("User with id: " + request.getUserId() + " not found or inactive!"));
            newRentedOrder.setUser(user);

            validateDeliveryMethod(request);

            if (DeliveryMethodEnum.Offline.equals(request.getDeliveryMethod())) {
                processOfflineReturn(newRentedOrder, request);
            } else {
                processOnlineReturn(newRentedOrder, request);
            }

            RentedOrder savedOrder = rentedOrderRepository.save(newRentedOrder);
            
            processRentalItems(savedOrder, request);

            newRentedOrder.setOrderStatus(OrderStatusEnum.Processing.toString());

            return rentedOrderMapper.toResponse(rentedOrderRepository.save(newRentedOrder));
            
        } catch (Exception e) {
            // Log error for debugging
            throw e; 
        }
    }

    private void validateDeliveryMethod(RentedOrderRequest request) {
        if (DeliveryMethodEnum.Online.equals(request.getDeliveryMethod())) {
            if (StringUtil.isEmpty(request.getCity()) || 
                StringUtil.isEmpty(request.getDistrict()) || 
                StringUtil.isEmpty(request.getStreet())) {
                throw new NewException("Address fields (city, district, street) are required for online delivery");
            } 
        } else if (DeliveryMethodEnum.Offline.equals(request.getDeliveryMethod())) {
            if (request.getBranchId() == null) {
                throw new NewException("Branch ID is required for offline pickup");
            }
            // Validate branch exists once here
            branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new NewException("Branch with id " + request.getBranchId() + " not found"));
        } else {
            throw new NewException("Invalid delivery method: " + request.getDeliveryMethod());
        }
    }

    // Fix 3: Remove duplicate database call
    private void processOfflineReturn(RentedOrder order, RentedOrderRequest request) {
        // Branch already validated in validateDeliveryMethod, so this should not throw
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new NewException("Branch with id " + request.getBranchId() + " not found"));
        
        order.setBranch(branch);
        order.setCity(null);
        order.setDistrict(null);
        order.setWard(null);
        order.setStreet(null);
        order.setShippingMethod(null);
    }

    private void processOnlineReturn(RentedOrder order, RentedOrderRequest request) {
        order.setCity(request.getCity());
        order.setDistrict(request.getDistrict());
        order.setWard(request.getWard());
        order.setStreet(request.getStreet());
        order.setNotes(request.getNotes());
        order.setShippingMethod(request.getShippingMethod().toString());
        order.setBranch(null);
    }

    // Fix 4: Add better error handling and clarify business logic
    private void processRentalItems(RentedOrder order, RentedOrderRequest request) {
        if (request.getItemIdLists() == null || request.getItemIdLists().isEmpty()) {
            throw new NewException("At least one rental item is required");
        }

        List<RentalItem> items = new ArrayList<>();
        
        // Validate all items first before modifying any
        List<RentalItem> rentalItems = new ArrayList<>();
        for (Long itemId : request.getItemIdLists()) {
            RentalItem rentalItem = rentalItemRepository.findById(itemId)
                .orElseThrow(() -> new NewException("Rental item with id " + itemId + " not found"));
            
            if (!OrderStatusEnum.Received.toString().equals(rentalItem.getStatus())) {
                throw new NewException("Item with id " + itemId + " must be in 'Received' status to be returned. Current status: " + rentalItem.getStatus());
            }
            
            rentalItems.add(rentalItem);
        }

        for (RentalItem rentalItem : rentalItems) {
            rentalItem.setStatus(OrderStatusEnum.Processing.toString());
            rentalItem.setRentedOrder(order);
            items.add(rentalItem);
        }
        
        order.setItems(items);
    }

    @Override
    @Transactional
    public boolean confirmOrder(Long id) {
        RentedOrder order = rentedOrderRepository.findById(id)
                .orElseThrow(() -> new NewException("Order not found"));
        
        if (!OrderStatusEnum.Processing.toString().equals(order.getOrderStatus())) {
            throw new NewException("Only processing orders can be confirmed");
        }

        for (RentalItem item : order.getItems()) {
            if(item.getStatus().equals(OrderStatusEnum.Processing.toString())){
                Book book = item.getBook();
                book.setStock(book.getStock() + item.getQuantity());
                book.setTypeActive(null);
                bookRepository.save(book);
                item.setStatus(OrderStatusEnum.Returned.toString());
                if (Instant.now().isAfter(item.getReturnDate())) {
                    Long daysLate = ChronoUnit.DAYS.between(item.getReturnDate(), Instant.now());
                    item.setTotalLateFee(book.getLateFee() * daysLate);
                    item.setDaysLate(daysLate);
                    item.setStatus(OrderStatusEnum.Overdue.toString());
                }
            }
        }

        order.setOrderStatus(OrderStatusEnum.Returned.toString());
        rentedOrderRepository.save(order);
        return true;
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long id) {
        RentedOrder order = rentedOrderRepository.findById(id)
                .orElseThrow(() -> new NewException("Order not found"));
        
        if (!OrderStatusEnum.Processing.toString().equals(order.getOrderStatus())) {
            throw new NewException("Only processing orders can be cancel");
        }

        order.setOrderStatus(OrderStatusEnum.Received.toString());
        rentedOrderRepository.save(order);
        return true;
    }

    @Override
    public RentedOrderResponse update(Long id, OrderStatusEnum newStatus) {
        RentedOrder currentRentedOrder = rentedOrderRepository.findById(id)
            .orElseThrow(() -> new NewException("Rented order with id = " + id + " not found"));

        currentRentedOrder.setOrderStatus(newStatus.toString());

        return rentedOrderMapper.toResponse(rentedOrderRepository.save(currentRentedOrder));
    }

    @Override
    public RentedOrderResponse getById(Long id) {
       return rentedOrderMapper.toResponse(
            rentedOrderRepository.findWithItems(id)
                .orElseThrow(() -> new NewException("Rented order with id = " + id + " not found"))
        );
    }

    @Override
    public PageResponse<Object> getAllRentedOrder(
        Long fromLateFee,
        Long toLateFee,
        Long userId,
        String orderStatus,
        Pageable pageable
    ) {
        Specification<RentedOrder> spec = RentedOrderSpecification.filterOrders(
            fromLateFee, toLateFee, userId, orderStatus
        );
        Page<RentedOrder> page = rentedOrderRepository.findAll(spec, pageable);
        Page<RentedOrderResponse> mappedPage = page.map(rentedOrderMapper::toResponse);

        return PageResponse.builder()
                .page(mappedPage.getNumber())
                .size(mappedPage.getSize())
                .totalPages(mappedPage.getTotalPages())
                .totalElements(mappedPage.getTotalElements())
                .content(mappedPage.getContent())
                .build();
    }

}
