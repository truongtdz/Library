package com.build.core_restful.domain.listener;

import java.time.Instant;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.build.core_restful.domain.RentedOrder;
import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.User;
import com.build.core_restful.service.NotificationService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.system.JwtUtil;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;

@Component
public class RentedOrderListener {
    private final NotificationService notificationService;
    private final UserService userService;
    
    public RentedOrderListener(
        @Lazy NotificationService notificationService,
        @Lazy UserService userService
    ) {
        this.notificationService = notificationService;
        this.userService = userService;
    };

    @PrePersist
    public void afterCreate(RentedOrder rentedOrder) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            User user = userService.getUserByEmail(currentUsername);

            Notification notification = Notification.builder()
                    .createByUser(user)
                    .active("CREATE")
                    .description("Đơn trả sách mới đã được thêm: " + rentedOrder.getId() + 
                               " bởi " + currentUsername)
                    .build();
            
            notificationService.createNotification(notification);

            rentedOrder.setCreateBy(currentUsername);
            rentedOrder.setCreateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for rentedOrder creation: " + e.getMessage());
        }
    }

    @PostUpdate
    public void afterUpdate(RentedOrder rentedOrder) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            String description = "Đơn trả sách đã được cập nhật trạng thái: " + rentedOrder.getOrderStatus() + 
                            " (ID: " + rentedOrder.getId() + ") bởi " + currentUsername;
            
            Notification notification = Notification.builder()
                    .active("UPDATE")
                    .description(description)
                    .build();
            
            notificationService.createNotification(notification);
            rentedOrder.setUpdateBy(currentUsername);
            rentedOrder.setUpdateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for rentedOrder update: " + e.getMessage());
        }
    } 
}