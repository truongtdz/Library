package com.build.core_restful.repository;

import com.build.core_restful.domain.RentalOrder;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {
    Page<RentalOrder> findAll(Specification<RentalOrder> spec, Pageable pageable);

    @Query("SELECT COUNT(ro) FROM RentalOrder ro WHERE ro.createAt BETWEEN :start AND :end AND ro.orderStatus = :status")
    Integer countRentalOrdersByStatusBetween(
        @Param("start") Instant start,
        @Param("end") Instant end,
        @Param("status") String orderStatus
    );

    @Query("SELECT SUM(ro.totalPrice) FROM RentalOrder ro WHERE ro.createAt BETWEEN :start AND :end AND ro.orderStatus = 'Returned'")
    Integer getRevenueRentalOrder(
        @Param("start") Instant start,
        @Param("end") Instant end
    );

    @Query("SELECT SUM(ro.depositPrice) FROM RentalOrder ro WHERE ro.createAt BETWEEN :start AND :end AND ro.orderStatus != 'Cancelled' AND ro.orderStatus != 'Returned'")
    Integer getTotalDepositOrder(
        @Param("start") Instant start,
        @Param("end") Instant end
    );
}
