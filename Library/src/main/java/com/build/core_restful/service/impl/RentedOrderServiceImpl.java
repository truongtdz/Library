package com.build.core_restful.service.impl;

import com.build.core_restful.domain.*;
import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentedOrderResponse;
import com.build.core_restful.repository.*;
import com.build.core_restful.repository.specification.RentedOrderSpecification;
import com.build.core_restful.service.RentedOrderService;
import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.enums.UserStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.RentedOrderMapper;

import java.time.Instant;
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
    private final RentedOrderMapper rentedOrderMapper;

    public RentedOrderServiceImpl(RentedOrderRepository rentedOrderRepository,
                                    UserRepository userRepository,
                                    RentalItemRepository rentalItemRepository,
                                    RentedOrderMapper rentedOrderMapper) {
        this.rentedOrderRepository = rentedOrderRepository;
        this.userRepository = userRepository;
        this.rentalItemRepository = rentalItemRepository;
        this.rentedOrderMapper = rentedOrderMapper;
    }

    @Override
    @Transactional
    public RentedOrderResponse create(RentedOrderRequest request) {
        RentedOrder newRentedOrder = rentedOrderMapper.toEntity(request);

        newRentedOrder.setOrderStatus(OrderStatusEnum.Delivering.toString());
        newRentedOrder.setUser(userRepository.findByIdAndStatus(
            request.getUserId(), UserStatusEnum.Active.toString()
        ));

        List<RentalItem> items = new ArrayList<>();
        Long totalLateFee = 0L;

        for (Long item : request.getItemIdLists()) {
            RentalItem rentalItem = rentalItemRepository.findById(item)
                .orElseThrow(() -> new NewException("item with id: " + item + " not found"));

            if (Instant.now().isAfter(rentalItem.getRentedDate())) {
                totalLateFee += rentalItem.getTotalRental() / 2;
            }

            rentalItem.setRentedOrder(newRentedOrder);
            items.add(rentalItem);
        }

        newRentedOrder.setLateFee(totalLateFee);
        newRentedOrder.setItems(items);

        return rentedOrderMapper.toResponse(rentedOrderRepository.save(newRentedOrder));
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
