package com.build.core_restful.service.impl;

import org.springframework.stereotype.Service;

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
    @Override
    public void createNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);;
    }
    
}
