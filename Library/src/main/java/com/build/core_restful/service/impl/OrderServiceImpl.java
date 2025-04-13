package com.build.core_restful.service.impl;

import com.build.core_restful.domain.RentalOrder;
import com.build.core_restful.domain.request.OrderRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.OrderResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.OrderRepository;
import com.build.core_restful.service.OrderService;
import com.build.core_restful.util.mapper.OrderMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public boolean existOrderById(Long id) {
        return orderRepository.existsById(id);
    }

    @Override
    public PageResponse<Object> getAllOrders(Pageable pageable) {
        Page<OrderResponse> page = orderRepository.findAll(pageable).map(orderMapper::toOrderResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .content(page.getContent())
                .build();
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderResponse)
                .orElse(null);
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        RentalOrder order = orderMapper.toOrder(orderRequest);
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse updateOrder(OrderRequest orderRequest) {
        RentalOrder order = orderRepository.findById(orderRequest.getId()).orElse(null);
        if (order != null) {
            orderMapper.updateOrder(order, orderRequest);
            return orderMapper.toOrderResponse(orderRepository.save(order));
        }
        return null;
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
