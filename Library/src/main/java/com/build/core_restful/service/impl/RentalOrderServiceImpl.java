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
        processRentalItems(order, request);
        
        return rentalOrderMapper.toResponse(rentalOrderRepository.save(order));
    }

    private void validateDeliveryMethod(RentalOrderRequest request) {
        if (DeliveryMethodEnum.Online.equals(request.getDeliveryMethod())) {
            if (StringUtil.isEmpty(request.getCity()) || 
                StringUtil.isEmpty(request.getDistrict()) || 
                StringUtil.isEmpty(request.getStreet())) {
                throw new NewException("Address is required for online return");
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
        
        order.setReceiveDay(request.getReceiveDay()); 

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
        
        Long timeDelivery = ShippingMethodEnum.Express.equals(request.getShippingMethod()) ? 1L : 3L;
        order.setReceiveDay(Instant.now().plus(timeDelivery, ChronoUnit.DAYS));
        order.setShippingMethod(request.getShippingMethod().toString());
        
        order.setBranch(null);
    }

    @Transactional
    private void processRentalItems(RentalOrder order, RentalOrderRequest request) {
        Instant now = Instant.now();
        List<RentalItem> items = new ArrayList<>();
        
        Long timeDelivery = getDeliveryTime(request);
        
        for (RentalItemRequest item : request.getItems()) {
            Book book = bookRepository.findById(item.getBookId())
                    .orElseThrow(() -> new NewException("Book id= " + item.getBookId() + " not found"));

            if (book.getStock() < item.getQuantity()) {
                throw new NewException("Insufficient stock for book: " + book.getId() + " and stock: " + book.getStock());
            }

            Long actualRentalDays = item.getRentedDay() - timeDelivery;
            if (actualRentalDays <= 0) {
                throw new NewException("Rental days must be greater than delivery time");
            }
            
            Long itemRental = book.getRentalPrice() * item.getQuantity() * actualRentalDays;
            Long itemDeposit = book.getDepositPrice() * item.getQuantity();

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
                    .rentalDate(now)
                    .rentedDate(order.getReceiveDay().plus(item.getRentedDay(), ChronoUnit.DAYS))
                    .rentalPrice(book.getRentalPrice())
                    .depositPrice(book.getDepositPrice())
                    .totalRental(itemRental)
                    .totalDeposit(itemDeposit)
                    .status(order.getOrderStatus())
                    .rentalOrder(order)
                    .book(book)
                    .build();

            book.setStock(book.getStock() - item.getQuantity());
            book.setQuantityRented(book.getQuantityRented() + item.getQuantity());
            bookRepository.save(book);

            items.add(rentalItem);
        }
        
        order.setItems(items);
    }

    private Long getDeliveryTime(RentalOrderRequest request) {
        if (DeliveryMethodEnum.Offline.equals(request.getDeliveryMethod())) {
            return 0L; 
        }
        
        return ShippingMethodEnum.Express.equals(request.getShippingMethod()) ? 1L : 3L;
    }
        
    @Override
    public RentalOrderResponse update(Long id, OrderStatusEnum newStatus) {
        RentalOrder order = rentalOrderRepository.findById(id)
                .orElseThrow(() -> new NewException("Order not found"));
        order.setOrderStatus(newStatus.toString());
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
