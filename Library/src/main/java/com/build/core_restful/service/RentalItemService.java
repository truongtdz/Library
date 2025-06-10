package com.build.core_restful.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.response.PageResponse;

@Service
public interface RentalItemService {
    void sendEmailLateBook();

    PageResponse<Object> getItemByUser(
        String status,
        Long userId, Pageable pageable 
    );
}
