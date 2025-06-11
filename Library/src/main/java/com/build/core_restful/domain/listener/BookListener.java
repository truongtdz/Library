package com.build.core_restful.domain.listener;

import java.time.Instant;
import java.util.Arrays;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.User;
import com.build.core_restful.service.NotificationService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.system.JwtUtil;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;

@Component
public class BookListener {
    private final NotificationService notificationService;
    private final UserService userService;
    
    public BookListener(
        @Lazy NotificationService notificationService,
        @Lazy UserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    };

    @PrePersist
    public void afterCreate(Book book) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            User user = userService.getUserByEmail(currentUsername);

            Notification notification = Notification.builder()
                    .createByUser(user)
                    .active(book.getTypeActive())
                    .description("Sách mới đã được thêm: " + book.getName() + 
                               " bởi " + currentUsername)
                    .build();
            
            notificationService.createNotification(notification);

            book.setCreateBy(currentUsername);
            book.setCreateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for book creation: " + e.getMessage());
        }
    }

    @PostUpdate
    public void afterUpdate(Book book) {
        try {
            if (!Arrays.asList("DELETE", "RESTORE", "UPDATE").contains(book.getTypeActive())) {
                return;
            }
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            String description;

            if (book.getTypeActive().equals("DELETE")) {
                description = "Sách đã được xóa: " + book.getName() + 
                            " (ID: " + book.getId() + ") bởi " + currentUsername;
            } else if (book.getTypeActive().equals("RESTORE")) {
                description = "Sách đã được khôi phục: " + book.getName() + 
                            " (ID: " + book.getId() + ") bởi " + currentUsername;
            } else {
                description = "Sách đã được cập nhật: " + book.getName() + 
                            " (ID: " + book.getId() + ") bởi " + currentUsername;
            }
            
            Notification notification = Notification.builder()
                    .active(book.getTypeActive())
                    .description(description)
                    .build();
            
            notificationService.createNotification(notification);
            book.setUpdateBy(currentUsername);
            book.setUpdateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for book update: " + e.getMessage());
        }
    } 
}