package com.build.core_restful.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.build.core_restful.domain.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE " +
           "(:email IS NULL OR n.email = :email) AND " +
           "(:active IS NULL OR n.active = :active)")
    Page<Notification> findNotificationsWithOptionalFilters(
        @Param("email") String email, 
        @Param("active") String active, 
        Pageable pageable
    );
}