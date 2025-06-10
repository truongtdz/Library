package com.build.core_restful.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.util.enums.OrderStatusEnum;

@Service
public interface RentalItemService {
    void sendEmailLateBook();

    PageResponse<Object> getItemByUser(
        OrderStatusEnum itemStatus,
        Long userId, Pageable pageable 
    );
}
