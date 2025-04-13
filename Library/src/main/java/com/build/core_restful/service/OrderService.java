package com.build.core_restful.service;

import com.build.core_restful.domain.request.OrderRequest;
import com.build.core_restful.domain.response.OrderResponse;
import com.build.core_restful.domain.response.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    boolean existOrderById(Long id);
    PageResponse<Object> getAllOrders(Pageable pageable);
    OrderResponse getOrderById(Long id);
    OrderResponse createOrder(OrderRequest order);
    OrderResponse updateOrder(OrderRequest order);
    void deleteOrder(Long id);
}
