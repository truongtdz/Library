package com.build.core_restful.repository;

import com.build.core_restful.domain.RentalOrder;

import java.time.Instant;
import java.util.List;

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

    List<RentalOrder> findAllByUserId(Long id);

    List<RentalOrder> findAllByBranchId(Long id);

    @Query("SELECT SUM(ro.totalPrice) FROM RentalOrder ro WHERE DATE(ro.createAt) = DATE(:date) AND ro.orderStatus = 'Returned'")
    Long getRevenueRentalOrderByDate(
        @Param("date") Instant date
    );

    @Query("SELECT SUM(ro.depositPrice) FROM RentalOrder ro WHERE DATE(ro.createAt) = DATE(:date) AND ro.orderStatus != 'Cancelled' AND ro.orderStatus != 'Returned'")
    Long getTotalDepositOrderByDate(
        @Param("date") Instant date
    );

    @Query("SELECT COUNT(ro) FROM RentalOrder ro WHERE DATE(ro.createAt) = DATE(:date)")
    Long countAllRentalOrdersByDate(
        @Param("date") Instant date
    );
}
