package com.build.core_restful.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.response.PageResponse;

@Service
public interface NotificationService {
    void createNotification(Notification notification);

    PageResponse<Object> getAllNotification(String email, String active, Pageable pageable);

    void deleteNotification(Long id);
}
