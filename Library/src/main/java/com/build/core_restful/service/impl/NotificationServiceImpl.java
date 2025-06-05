package com.build.core_restful.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.build.core_restful.domain.Notification;
import com.build.core_restful.repository.NotificationRepository;
import com.build.core_restful.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl (
        NotificationRepository notificationRepository
    ){
        this.notificationRepository = notificationRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNotification(Notification notification) {
        try {
            notificationRepository.save(notification);
        } catch (Exception e) {
            System.err.println("Failed to save notification: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteNotification(Long id) {
        try {
            notificationRepository.deleteById(id);
        } catch (Exception e) {
            System.err.println("Failed to delete notification: " + e.getMessage());
        }
    }
    
}
