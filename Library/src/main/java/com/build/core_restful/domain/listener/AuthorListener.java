package com.build.core_restful.domain.listener;

import java.time.Instant;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.build.core_restful.domain.Author;
import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.User;
import com.build.core_restful.service.NotificationService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.system.JwtUtil;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;

@Component
public class AuthorListener {
    private final NotificationService notificationService;
    private final UserService userService;
    
    public AuthorListener(
        @Lazy NotificationService notificationService,
        @Lazy UserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    };

    @PrePersist
    public void afterCreate(Author author) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            User user = userService.getUserByEmail(currentUsername);

            Notification notification = Notification.builder()
                    .createByUser(user)
                    .active(author.getTypeActive())
                    .description("Tác giả mới đã được thêm: " + author.getName() + 
                               " bởi " + currentUsername)
                    .build();
            
            notificationService.createNotification(notification);

            author.setCreateBy(currentUsername);
            author.setCreateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for author creation: " + e.getMessage());
        }
    }

    @PostUpdate
    public void afterUpdate(Author author) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            String description;

            if (author.getTypeActive().equals("DELETE")) {
                description = "Tác giả đã được xóa: " + author.getName() + 
                            " (ID: " + author.getId() + ") bởi " + currentUsername;
            } else if (author.getTypeActive().equals("RESTORE")) {
                description = "Tác giả đã được khôi phục: " + author.getName() + 
                            " (ID: " + author.getId() + ") bởi " + currentUsername;
            } else {
                description = "Tác giả đã được cập nhật: " + author.getName() + 
                            " (ID: " + author.getId() + ") bởi " + currentUsername;
            }
            
            Notification notification = Notification.builder()
                    .active(author.getTypeActive())
                    .description(description)
                    .build();
            
            notificationService.createNotification(notification);
            author.setUpdateBy(currentUsername);
            author.setUpdateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for author update: " + e.getMessage());
        }
    } 
}