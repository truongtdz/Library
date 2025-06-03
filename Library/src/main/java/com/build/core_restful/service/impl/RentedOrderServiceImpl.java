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
import com.build.core_restful.util.enums.ItemStatusEnum;
import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.enums.ShippingMethodEnum;
import com.build.core_restful.util.enums.UserStatusEnum;
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
        RentedOrder newRentedOrder = rentedOrderMapper.toEntity(request);

        User user = userRepository.findByIdAndStatus(request.getUserId(), UserStatusEnum.Active.toString())
                        .orElseThrow(() -> new NewException("User have id: " + request.getUserId() + " not exist!"));
        if (user == null) {
            throw new NewException("Active user not found with id: " + request.getUserId());
        }
        newRentedOrder.setUser(user);

        validateDeliveryMethod(request);

        if (DeliveryMethodEnum.Offline.equals(request.getDeliveryMethod())) {
            processOfflineReturn(newRentedOrder, request);
        } else {
            processOnlineReturn(newRentedOrder, request);
        }

        processRentalItems(newRentedOrder, request);

        newRentedOrder.setOrderStatus(OrderStatusEnum.Delivering.toString());

        return rentedOrderMapper.toResponse(rentedOrderRepository.save(newRentedOrder));
    }

    private void validateDeliveryMethod(RentedOrderRequest request) {
        if (DeliveryMethodEnum.Online.equals(request.getDeliveryMethod())) {
            if (StringUtil.isEmpty(request.getCity()) || 
                StringUtil.isEmpty(request.getDistrict()) || 
                StringUtil.isEmpty(request.getStreet())) {
                throw new NewException("Address is required for online return");
            } 
            if (request.getShippingMethod() == null) {
                throw new NewException("Shipping method is required for online return");
            }
        } else {
            if (request.getBranchId() == null) {
                throw new NewException("Branch is required for offline return");
            }

            branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new NewException("Branch not found"));
        }
    }

    private void processOfflineReturn(RentedOrder order, RentedOrderRequest request) {
        order.setBranch(branchRepository.findById(request.getBranchId())
                        .orElseThrow(() -> new NewException("Branch with id = " + request.getBranchId() + " not found"))
        );
        
        order.setRentedDay(request.getRentedDay());

        order.setCity(null);
        order.setDistrict(null);
        order.setWard(null);
        order.setStreet(null);

        order.setShippingMethod(null);

        order.setOrderStatus(OrderStatusEnum.Delivering.toString());
    }

    private void processOnlineReturn(RentedOrder order, RentedOrderRequest request) {
        order.setCity(request.getCity());
        order.setDistrict(request.getDistrict());
        order.setWard(request.getWard());
        order.setStreet(request.getStreet());
        order.setNotes(request.getNotes());
        order.setShippingMethod(request.getShippingMethod().toString());
        
        Long deliveryTime = ShippingMethodEnum.Express.equals(request.getShippingMethod()) ? 1L : 3L;
        order.setRentedDay(Instant.now().plus(deliveryTime, ChronoUnit.DAYS));
        
        order.setBranch(null);
        
        order.setOrderStatus(OrderStatusEnum.Delivering.toString());
    }

    private void processRentalItems(RentedOrder order, RentedOrderRequest request) {
        List<RentalItem> items = new ArrayList<>();
        Long totalLateFee = 0L;
        Instant now = Instant.now();

        for (Long itemId : request.getItemIdLists()) {
            RentalItem rentalItem = rentalItemRepository.findById(itemId)
                .orElseThrow(() -> new NewException("Rental item with id: " + itemId + " not found"));

            if (!ItemStatusEnum.Rented.toString().equals(rentalItem.getItemStatus())) {
                throw new NewException("Item " + itemId + " is not in rented status");
            }

            if (now.isAfter(rentalItem.getRentedDate())) {
                Long daysLate = ChronoUnit.DAYS.between(rentalItem.getRentedDate(), now);
                Long lateFeePerDay = rentalItem.getRentalPrice() / 2; // 50% rental price per day
                totalLateFee += lateFeePerDay * daysLate * rentalItem.getQuantity();
            }

            rentalItem.setItemStatus(ItemStatusEnum.Returned.toString());
            rentalItem.setRentedOrder(order);
            
            Book book = rentalItem.getBook();
            book.setStock(book.getStock() + rentalItem.getQuantity());
            book.setQuantityRented(book.getQuantityRented() - rentalItem.getQuantity());
            bookRepository.save(book);
            
            items.add(rentalItem);
        }

        order.setLateFee(totalLateFee);
        order.setItems(items);
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
