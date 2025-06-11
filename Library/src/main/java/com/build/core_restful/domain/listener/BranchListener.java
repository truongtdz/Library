package com.build.core_restful.domain.listener;

import java.time.Instant;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.build.core_restful.domain.Branch;
import com.build.core_restful.domain.Notification;
import com.build.core_restful.domain.User;
import com.build.core_restful.service.NotificationService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.system.JwtUtil;

import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;

@Component
public class BranchListener {
    private final NotificationService notificationService;
    
    public BranchListener(
        @Lazy NotificationService notificationService
    ) {
        this.notificationService = notificationService;
    };

    @PrePersist
    public void afterCreate(Branch branch) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");

            Notification notification = Notification.builder()
                    .email(currentUsername)
                    .active(branch.getTypeActive())
                    .description("Chi nhánh mới đã được thêm: " + branch.getName() + 
                               " bởi " + currentUsername)
                    .build();
            
            notificationService.createNotification(notification);

            branch.setCreateBy(currentUsername);
            branch.setCreateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for branch creation: " + e.getMessage());
        }
    }

    @PostUpdate
    public void afterUpdate(Branch branch) {
        try {
            String currentUsername = JwtUtil.getCurrentUserLogin().orElse("System");
            
            String description;

            if (branch.getTypeActive().equals("DELETE")) {
                description = "Chi nhánh đã được xóa: " + branch.getName() + 
                            " (ID: " + branch.getId() + ") bởi " + currentUsername;
            } else if (branch.getTypeActive().equals("RESTORE")) {
                description = "Chi nhánh đã được khôi phục: " + branch.getName() + 
                            " (ID: " + branch.getId() + ") bởi " + currentUsername;
            } else {
                description = "Chi nhánh đã được cập nhật: " + branch.getName() + 
                            " (ID: " + branch.getId() + ") bởi " + currentUsername;
            }
            
            Notification notification = Notification.builder()
                    .email(currentUsername)
                    .active(branch.getTypeActive())
                    .description(description)
                    .build();
            
            notificationService.createNotification(notification);
            branch.setUpdateBy(currentUsername);
            branch.setUpdateAt(Instant.now());
        } catch (Exception e) {
            System.err.println("Error creating notification for branch update: " + e.getMessage());
        }
    } 
}