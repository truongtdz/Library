package com.build.core_restful.service;

import org.springframework.data.domain.Pageable;

import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentedOrderResponse;
import com.build.core_restful.util.enums.OrderStatusEnum;

public interface RentedOrderService {
    RentedOrderResponse create(RentedOrderRequest request);

    RentedOrderResponse update(Long id, OrderStatusEnum newStatus);

    RentedOrderResponse getById(Long id);
    
    PageResponse<Object> getAllRentedOrder(
        Long fromLateFee,
        Long toLateFee,
        Long userId,
        String orderStatus,
        Pageable pageable
    );

    boolean confirmOrder(Long id);

    boolean cancelOrder(Long id);
}
