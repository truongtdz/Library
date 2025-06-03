package com.build.core_restful.repository;

import com.build.core_restful.domain.RevenueReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
@Repository
public interface RevenueReportRepository extends JpaRepository<RevenueReports, Long> {
    List<RevenueReports> findByDateBetweenOrderByDateDesc(Instant startDate, Instant endDate);

    List<RevenueReports> findAllByOrderByDateDesc();

    @Query("SELECT r FROM RevenueReports r WHERE r.date >= :startDate AND r.date <= :endDate ORDER BY r.date DESC")
    List<RevenueReports> findRevenueReportsByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.quantityRentalOrder), 0) FROM RevenueReports r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumQuantityRentalOrdersByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalLateFee), 0) FROM RevenueReports r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalLateFeeByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalRental), 0) FROM RevenueReports r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalRentalByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalDeposit), 0) FROM RevenueReports r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalDepositByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalRevenue), 0) FROM RevenueReports r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalRevenueByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}