package com.build.core_restful.service;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Notification;

@Service
public interface NotificationService {
    void createNotification(Notification notification);

    void deleteNotification(Long id);
}
