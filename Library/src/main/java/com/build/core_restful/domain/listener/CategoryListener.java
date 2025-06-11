package com.build.core_restful.domain.listener;

import java.time.Instant;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.build.core_restful.domain.Category;
import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.User;
import com.build.core_restful.service.NotificationService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.system.JwtUtil;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;

@Component
public class CategoryListener {
    private final NotificationService notificationService;
    private final UserService userService;
    
    public CategoryListener(
        @Lazy NotificationService notificationService,
        @Lazy UserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    };

    @PrePersist
    public void afterCreate(Category category) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            User user = userService.getUserByEmail(currentUsername);

            Notification notification = Notification.builder()
                    .createByUser(user)
                    .active(category.getTypeActive())
                    .description("Danh mục mới đã được thêm: " + category.getName() + 
                               " bởi " + currentUsername)
                    .build();
            
            notificationService.createNotification(notification);

            category.setCreateBy(currentUsername);
            category.setCreateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for category creation: " + e.getMessage());
        }
    }

    @PostUpdate
    public void afterUpdate(Category category) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            String description;

            if (category.getTypeActive().equals("DELETE")) {
                description = "Danh mục đã được xóa: " + category.getName() + 
                            " (ID: " + category.getId() + ") bởi " + currentUsername;
            } else if (category.getTypeActive().equals("RESTORE")) {
                description = "Danh mục đã được khôi phục: " + category.getName() + 
                            " (ID: " + category.getId() + ") bởi " + currentUsername;
            } else {
                description = "Danh mục đã được cập nhật: " + category.getName() + 
                            " (ID: " + category.getId() + ") bởi " + currentUsername;
            }
            
            Notification notification = Notification.builder()
                    .active(category.getTypeActive())
                    .description(description)
                    .build();
            
            notificationService.createNotification(notification);
            category.setUpdateBy(currentUsername);
            category.setUpdateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for category update: " + e.getMessage());
        }
    } 
}