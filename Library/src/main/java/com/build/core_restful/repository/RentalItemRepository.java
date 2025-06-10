package com.build.core_restful.repository;

import com.build.core_restful.domain.RentalItem;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalItemRepository extends JpaRepository<RentalItem, Long> {
    List<RentalItem> findAllByBookId(Long id);
    
    @Query("SELECT ri FROM RentalItem ri WHERE ri.rentedDate BETWEEN :now AND :threeDaysLater AND ri.status = :status")
    List<RentalItem> findItemsDueWithinThreeDaysByStatus(
        @Param("now") Instant now, 
        @Param("threeDaysLater") Instant threeDaysLater, 
        @Param("status") String status
    );

    Page<RentalItem> findByStatusAndUserId(String status, Long userId, Pageable pageable);
    Page<RentalItem> findByStatus(String status, Pageable pageable);
}
