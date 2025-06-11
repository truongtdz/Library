package com.build.core_restful.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.response.NotificationResponse;
import com.build.core_restful.domain.response.PageResponse;
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

    @Override
    public PageResponse<Object> getAllNotification(String email, String active, Pageable pageable) {
        Page<Notification> page = notificationRepository.findNotificationsWithOptionalFilters(email, active, pageable);

        Page<NotificationResponse> pageResponse = page.map(notification -> NotificationResponse.builder()
                                    .createAt(notification.getCreateAt())
                                    .updateAt(notification.getUpdateAt())
                                    .id(notification.getId())
                                    .description(notification.getDescription())
                                    .build());

        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();  
    }
    
}
