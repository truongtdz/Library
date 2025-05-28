package com.build.core_restful.service.impl;

import com.build.core_restful.domain.*;
import com.build.core_restful.domain.request.RentalItemRequest;
import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentalOrderResponse;
import com.build.core_restful.repository.*;
import com.build.core_restful.repository.specification.RentalOrderSpecification;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.util.enums.ItemStatusEnum;
import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.enums.ShippingMethodEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.RentalOrderMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalOrderServiceImpl implements RentalOrderService {

    private final RentalOrderRepository rentalOrderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final RentalOrderMapper rentalOrderMapper;

    public RentalOrderServiceImpl(RentalOrderRepository rentalOrderRepository,
                                    UserRepository userRepository,
                                    BookRepository bookRepository,
                                    RentalOrderMapper rentalOrderMapper) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.rentalOrderMapper = rentalOrderMapper;
    }

    @Override
    public RentalOrderResponse create(RentalOrderRequest request) {
        RentalOrder order = rentalOrderMapper.toEntity(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NewException("User not found"));
        order.setUser(user);

        Long timeDelivery = request.getShippingMethod().equals(ShippingMethodEnum.Express) ? 3L : 1L;
        Instant now = Instant.now();
        List<RentalItem> items = new ArrayList<>();

        for (RentalItemRequest item : request.getItems()) {
            Book book = bookRepository.findById(item.getBookId())
                    .orElseThrow(() -> new NewException("Book id= " + item.getBookId() + " not found"));

            Long itemRental = book.getRentalPrice() * item.getQuantity() * (item.getRentedDay() - timeDelivery);
            Long itemDeposit = book.getDepositPrice() * item.getQuantity();

            RentalItem rentalItem = RentalItem.builder()
                    .quantity(item.getQuantity())
                    .bookName(book.getName())
                    .rentalDate(now)
                    .rentedDate(now.plus(item.getRentedDay(), ChronoUnit.DAYS))
                    .rentalPrice(book.getRentalPrice())
                    .depositPrice(book.getDepositPrice())
                    .totalRental(itemRental)
                    .totalDeposit(itemDeposit)
                    .itemStatus(ItemStatusEnum.Pending.toString())
                    .rentalOrder(order)
                    .book(book)
                    .build();

            book.setStock(book.getStock() - item.getQuantity());
            book.setQuantityRented(book.getQuantityRented() + item.getQuantity());
            bookRepository.save(book);

            items.add(rentalItem);
        }

        order.setItems(items);
        order.setReceiveDay(now.plus(timeDelivery, ChronoUnit.DAYS));
        order.setOrderStatus(OrderStatusEnum.Processing.toString());

        return rentalOrderMapper.toResponse(rentalOrderRepository.save(order));
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
        return rentalOrderMapper.toResponse(
                rentalOrderRepository.findById(id)
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
    public Integer getQuantityByOrderStatus(Instant startDate, Instant endDate, OrderStatusEnum orderStatus) {
        return rentalOrderRepository.countRentalOrdersByStatusBetween(startDate, endDate, orderStatus.toString());
    }

    @Override
    public Integer getRevenueRentalOrder(Instant startDate, Instant endDate) {
        return rentalOrderRepository.getRevenueRentalOrder(startDate, endDate);
    }

    @Override
    public Integer getTotalDepositOrder(Instant startDate, Instant endDate) {
        return rentalOrderRepository.getTotalDepositOrder(startDate, endDate);
    }

}
