package com.build.core_restful.repository;

import com.build.core_restful.domain.RentalItem;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalItemRepository extends JpaRepository<RentalItem, Long> {
    List<RentalItem> findAllByBookId(Long id);
    
    @Query("SELECT ri FROM RentalItem ri WHERE ri.rentedDate BETWEEN :now AND :threeDaysLater AND ri.itemStatus = :itemStatus")
    List<RentalItem> findItemsDueWithinThreeDaysByStatus(
        @Param("now") Instant now, 
        @Param("threeDaysLater") Instant threeDaysLater, 
        @Param("itemStatus") String itemStatus
    );
}
