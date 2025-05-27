package com.build.core_restful.service.impl;

import com.build.core_restful.domain.*;
import com.build.core_restful.domain.request.RentalItemRequest;
import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentalOrderResponse;
import com.build.core_restful.repository.*;
import com.build.core_restful.repository.specification.RentalOrderSpecification;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.enums.ShippingMethod;
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
    private final RentalItemRepository rentalItemRepository;
    private final RentalOrderMapper rentalOrderMapper;

    public RentalOrderServiceImpl(RentalOrderRepository rentalOrderRepository,
                                    UserRepository userRepository,
                                    BookRepository bookRepository,
                                    RentalItemRepository rentalItemRepository,
                                    RentalOrderMapper rentalOrderMapper) {
        this.rentalOrderRepository = rentalOrderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.rentalItemRepository = rentalItemRepository;
        this.rentalOrderMapper = rentalOrderMapper;
    }

    @Override
    public RentalOrderResponse create(RentalOrderRequest request) {
        RentalOrder order = rentalOrderRepository.save(rentalOrderMapper.toEntity(request));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NewException("User not found"));
        order.setUser(user);

        Long timeDelivery = request.getShippingMethod().equals(ShippingMethod.Express) ? 3L : 1L;
        Instant now = Instant.now();
        Long totalRental = 0L;
        Long totalDeposit = 0L;
        List<RentalItem> items = new ArrayList<>();

        for (RentalItemRequest item : request.getItems()) {
            Book book = bookRepository.findById(item.getBookId())
                    .orElseThrow(() -> new NewException("Book id= " + item.getBookId() + " not found"));

            Long itemRental = book.getRentalPrice() * item.getQuantity() * (item.getRentedDay() - timeDelivery);
            Long itemDeposit = book.getDepositPrice() * item.getQuantity();

            RentalItem rentalItem = RentalItem.builder()
                    .quantity(item.getQuantity())
                    .rentalDate(now)
                    .rentedDay(item.getRentedDay())
                    .returnDate(now.plus(item.getRentedDay(), ChronoUnit.DAYS))
                    .bookName(book.getName())
                    .rentalPrice(book.getRentalPrice())
                    .depositPrice(book.getDepositPrice())
                    .lateFee(book.getLateFee())
                    .totalRental(itemRental)
                    .totalDeposit(itemDeposit)
                    .order(order)
                    .book(book)
                    .build();

            book.setStock(book.getStock() - item.getQuantity());
            rentalItemRepository.save(rentalItem);
            items.add(rentalItem);

            totalRental += itemRental;
            totalDeposit += itemDeposit;
        }

        order.setItems(items);
        order.setTotalPrice(totalRental);
        order.setDepositPrice(totalDeposit);
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

}
