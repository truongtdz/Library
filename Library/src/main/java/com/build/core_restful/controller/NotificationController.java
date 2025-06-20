package com.build.core_restful.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.NotificationService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.TypeActiveEnum;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(
        NotificationService notificationService
    ){
        this.notificationService = notificationService;
    }

    @GetMapping("/all")
    @AddMessage("Get all notifications")
    public ResponseEntity<PageResponse<Object>> getAllAddress(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String mail,
        @RequestParam(required = false) TypeActiveEnum typeActive
    ) {
        Pageable pageable = PageRequest.of(page, size);

        String active = null; String email = null;
        if(typeActive != null){
            active = typeActive.toString();
        }
        if(mail != null){
            email = mail;
        }

        return ResponseEntity.ok(notificationService.getAllNotification(
            email, active, pageable
        ));
    }
}
