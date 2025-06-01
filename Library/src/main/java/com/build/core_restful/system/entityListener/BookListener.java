package com.build.core_restful.system.entityListener;

import org.springframework.stereotype.Component;

import com.build.core_restful.service.NotificationService;

@Component
public class BookListener {
    private final NotificationService notificationService;

    public BookListener(
        NotificationService notificationService
    ){
        this.notificationService = notificationService;
    }

    
}
