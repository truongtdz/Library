package com.build.core_restful.service;

import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentalOrderResponse;
import com.build.core_restful.util.enums.OrderStatusEnum;
import org.springframework.data.domain.Pageable;

public interface RentalOrderService {
    RentalOrderResponse create(RentalOrderRequest request);
    RentalOrderResponse update(Long id, OrderStatusEnum newStatus);
    RentalOrderResponse getById(Long id);
    PageResponse<Object> getAll(Pageable pageable);
}
