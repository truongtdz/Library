package com.build.core_restful.service.impl;

import com.build.core_restful.domain.*;
import com.build.core_restful.domain.request.RentalItemRequest;
import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.domain.request.SaveRevenueEveryDay;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentalOrderResponse;
import com.build.core_restful.repository.*;
import com.build.core_restful.repository.specification.RentalOrderSpecification;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.util.StringUtil;
import com.build.core_restful.util.enums.DeliveryMethodEnum;
import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.enums.PaymentStatusEnum;
import com.build.core_restful.util.enums.ShippingMethodEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.RentalOrderMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalOrderServiceImpl implements RentalOrderService {
    private final RentalOrderRepository rentalOrderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BranchRepository branchRepository;
    private final RentalOrderMapper rentalOrderMapper;

    public RentalOrderServiceImpl(
        RentalOrderRepository rentalOrderRepository,
        UserRepository userRepository,
        BookRepository bookRepository,
        BranchRepository branchRepository,
        RentalOrderMapper rentalOrderMapper
    ) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.branchRepository = branchRepository;
        this.rentalOrderMapper = rentalOrderMapper;
    }

    @Override
    @Transactional
    public RentalOrderResponse create(RentalOrderRequest request) {
        RentalOrder order = rentalOrderMapper.toEntity(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NewException("User not found"));
        order.setUser(user);

        validateDeliveryMethod(request);

        if (DeliveryMethodEnum.Offline.equals(request.getDeliveryMethod())) {
            processOfflineDelivery(order, request);
        } else {
            processOnlineDelivery(order, request);
        }

        order.setOrderStatus(OrderStatusEnum.Processing.toString());
        
        createPendingRentalItems(order, request);
        
        return rentalOrderMapper.toResponse(rentalOrderRepository.save(order));
    }

    private void validateDeliveryMethod(RentalOrderRequest request) {
        if (DeliveryMethodEnum.Online.equals(request.getDeliveryMethod())) {
            if (StringUtil.isEmpty(request.getCity()) || 
                StringUtil.isEmpty(request.getDistrict()) || 
                StringUtil.isEmpty(request.getStreet())) {
                throw new NewException("Address is required for online delivery");
            }
        } else {
            if (request.getBranchId() == null) {
                throw new NewException("Branch is required for offline pickup");
            }
            
            branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new NewException("Branch not found"));
        }
    }

    private void processOfflineDelivery(RentalOrder order, RentalOrderRequest request) {
        order.setBranch(branchRepository.findById(request.getBranchId())
                        .orElseThrow(() -> new NewException("Branch with id = " + request.getBranchId() + " not found"))
        );
        
        order.setReceiveDay(null); 

        order.setCity(null);
        order.setDistrict(null);
        order.setWard(null);
        order.setStreet(null);
        order.setShippingMethod(null);
    }

    private void processOnlineDelivery(RentalOrder order, RentalOrderRequest request) {
        order.setCity(request.getCity());
        order.setDistrict(request.getDistrict());
        order.setWard(request.getWard());
        order.setStreet(request.getStreet());
        order.setNotes(request.getNotes());
        
        order.setReceiveDay(null);
        order.setShippingMethod(request.getShippingMethod().toString());
        
        order.setBranch(null);
    }

    @Transactional
    private void createPendingRentalItems(RentalOrder order, RentalOrderRequest request) {
        List<RentalItem> items = new ArrayList<>();
        
        for (RentalItemRequest item : request.getItems()) {
            Book book = bookRepository.findById(item.getBookId())
                    .orElseThrow(() -> new NewException("Book id= " + item.getBookId() + " not found"));

            if (book.getStock() < item.getQuantity()) {
                throw new NewException("Insufficient stock for book: " + book.getId() + " available: " + book.getStock());
            }

            RentalItem rentalItem = RentalItem.builder()
                    .quantity(item.getQuantity())
                    .userId(order.getUser().getId())
                    .bookName(book.getName())
                    .lateFee(book.getLateFee())
                    .imageUrl(
                        book.getImages().stream()
                            .filter(image -> "true".equals(image.getIsDefault()))
                            .findFirst()
                            .map(Image::getUrl)
                            .orElse(null)
                    )
                    .rentalDate(null)
                    .rentedDate(null) 
                    .rentalPrice(book.getRentalPrice())
                    .depositPrice(book.getDepositPrice())
                    .timeRental(item.getRentedDay())
                    .totalRental(book.getRentalPrice() * item.getQuantity()) 
                    .totalDeposit(book.getDepositPrice() * item.getQuantity())
                    .status(order.getOrderStatus())
                    .rentalOrder(order)
                    .book(book)
                    .build();

            items.add(rentalItem);
        }
        
        order.setItems(items);
    }

    @Override
    @Transactional
    public boolean confirmOrder(Long id) {
        RentalOrder order = rentalOrderRepository.findById(id)
                .orElseThrow(() -> new NewException("Order not found"));
        
        if (!OrderStatusEnum.Processing.toString().equals(order.getOrderStatus())) {
            throw new NewException("Only processing orders can be confirmed");
        }
        
        Instant now = Instant.now();

        if (DeliveryMethodEnum.Offline.equals(DeliveryMethodEnum.valueOf(order.getDeliveryMethod()))) {
            order.setReceiveDay(now);
        } else {
            Long deliveryTime = ShippingMethodEnum.Express.toString().equals(order.getShippingMethod()) ? 1L : 3L;
            order.setReceiveDay(now.plus(deliveryTime, ChronoUnit.DAYS));
        }
        
        Long deliveryTime = getDeliveryTime(order);
        
        for (RentalItem item : order.getItems()) {
            Book book = item.getBook();
            
            if (book.getStock() < item.getQuantity()) {
                throw new NewException("Insufficient stock for book: " + book.getId() + " available: " + book.getStock());
            }
            
            Long actualRentalDays = item.getTimeRental() - deliveryTime;
            if (actualRentalDays <= 0) {
                throw new NewException("Rental days must be greater than delivery time");
            }
            
            Long totalRental = item.getRentalPrice() * item.getQuantity() * actualRentalDays;
            
            item.setRentalDate(now);
            item.setRentedDate(order.getReceiveDay().plus(item.getTimeRental(), ChronoUnit.DAYS));
            item.setTotalRental(totalRental);
            item.setStatus(OrderStatusEnum.Renting.toString());
            
            book.setStock(book.getStock() - item.getQuantity());
            book.setQuantityRented(book.getQuantityRented() + item.getQuantity());
            book.setTypeActive(null);
            bookRepository.save(book);
        }
        
        order.setOrderStatus(OrderStatusEnum.Delivered.toString());
        rentalOrderRepository.save(order);
        
        return true;
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long id) {
        RentalOrder order = rentalOrderRepository.findById(id)
                .orElseThrow(() -> new NewException("Order not found"));
        
        if (OrderStatusEnum.Processing.toString().equals(order.getOrderStatus())) {
            for (RentalItem item : order.getItems()) {
                Book book = item.getBook();
                book.setStock(book.getStock() + item.getQuantity());
                book.setQuantityRented(book.getQuantityRented() - item.getQuantity());
                bookRepository.save(book);
            }
        }
        
        order.setOrderStatus(OrderStatusEnum.Cancelled.toString());
        for (RentalItem item : order.getItems()) {
            item.setStatus(OrderStatusEnum.Cancelled.toString());
        }
        
        rentalOrderRepository.save(order);
        return true;
    }

    private Long getDeliveryTime(RentalOrder order) {
        if (DeliveryMethodEnum.Offline.toString().equals(order.getDeliveryMethod())) {
            return 0L; 
        }
        
        return ShippingMethodEnum.Express.toString().equals(order.getShippingMethod()) ? 1L : 3L;
    }
        
    @Override
    public RentalOrderResponse updateOrderStatus(Long id, OrderStatusEnum newStatus) {
        RentalOrder order = rentalOrderRepository.findById(id)
                .orElseThrow(() -> new NewException("Order not found"));
        order.setOrderStatus(newStatus.toString());
        for (RentalItem item : order.getItems()) {
            if(!item.getStatus().equals(OrderStatusEnum.Returned.toString())){
                item.setStatus(order.getOrderStatus());
            }
        }
        return rentalOrderMapper.toResponse(rentalOrderRepository.save(order));
    }

    @Override
    public RentalOrderResponse updatePaymentStatus(Long id, PaymentStatusEnum newStatus) {
        RentalOrder order = rentalOrderRepository.findById(id)
                .orElseThrow(() -> new NewException("Order not found"));
        order.setPaymentStatus(newStatus.toString());
        return rentalOrderMapper.toResponse(rentalOrderRepository.save(order));
    }

    @Override
    public RentalOrderResponse getById(Long id) {
        return rentalOrderMapper.toResponse(rentalOrderRepository.findById(id)
                        .orElseThrow(() -> new NewException("Order not found"))
        );
    }

    @Override
    public PageResponse<Object> getAllOrder(
        Long fromTotalPrice,
        Long toTotalPrice,
        Long fromDepositPrice,
        Long toDepositPrice,
        Long userId,
        String orderStatus,
        Pageable pageable
    ) {
        Specification<RentalOrder> spec = RentalOrderSpecification.filterOrders(
            fromTotalPrice, toTotalPrice, fromDepositPrice, toDepositPrice, userId, orderStatus
        );
        Page<RentalOrder> page = rentalOrderRepository.findAll(spec, pageable);
        Page<RentalOrderResponse> mappedPage = page.map(rentalOrderMapper::toResponse);

        return PageResponse.builder()
                .page(mappedPage.getNumber())
                .size(mappedPage.getSize())
                .totalPages(mappedPage.getTotalPages())
                .totalElements(mappedPage.getTotalElements())
                .content(mappedPage.getContent())
                .build();
    }

    @Override
    public Long getQuantityByOrderStatus(Instant date) {
        return rentalOrderRepository.countAllRentalOrdersByDate(date);
    }

    @Override
    public Long getRevenueRentalOrder(Instant date) {
        return rentalOrderRepository.getRevenueRentalOrderByDate(date);
    }

    @Override
    public Long getTotalDepositOrder(Instant date) {
        return rentalOrderRepository.getTotalDepositOrderByDate(date);
    }

    @Override
    public SaveRevenueEveryDay getRevenueEveryDay() {
        Instant date = Instant.now();
        return SaveRevenueEveryDay.builder()
                .date(date)
                .totalDeposit(rentalOrderRepository.getTotalDepositOrderByDate(date))
                .totalRentalPrice(rentalOrderRepository.getRevenueRentalOrderByDate(date))
                .totalRevenue(rentalOrderRepository.getRevenueRentalOrderByDate(date))
                .build();
    }

}
