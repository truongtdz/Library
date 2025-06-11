package com.build.core_restful.domain.listener;

import java.time.Instant;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.build.core_restful.domain.RentalOrder;
import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.User;
import com.build.core_restful.service.NotificationService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.system.JwtUtil;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;

@Component
public class RentalOrderListener {
    private final NotificationService notificationService;
    private final UserService userService;
    
    public RentalOrderListener(
        @Lazy NotificationService notificationService,
        @Lazy UserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    };

    @PrePersist
    public void afterCreate(RentalOrder rentalOrder) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            User user = userService.getUserByEmail(currentUsername);

            Notification notification = Notification.builder()
                    .createByUser(user)
                    .active("CREATE")
                    .description("Đơn thuê sách mới đã được thêm bởi " + currentUsername)
                    .build();
            
            notificationService.createNotification(notification);

            rentalOrder.setCreateBy(currentUsername);
            rentalOrder.setCreateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for rentalOrder creation: " + e.getMessage());
        }
    }

    @PostUpdate
    public void afterUpdate(RentalOrder rentalOrder) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            String description = "Đơn thuê sách đã được cập nhật trạng thái: " + rentalOrder.getOrderStatus() + 
                            " (ID: " + rentalOrder.getId() + ") bởi " + currentUsername;
            
            Notification notification = Notification.builder()
                    .active("UPDATE")
                    .description(description)
                    .build();
            
            notificationService.createNotification(notification);
            rentalOrder.setUpdateBy(currentUsername);
            rentalOrder.setUpdateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for rentalOrder update: " + e.getMessage());
        }
    } 
}