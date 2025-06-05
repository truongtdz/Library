package com.build.core_restful.repository;

import com.build.core_restful.domain.RevenueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RevenueReportRepository extends JpaRepository<RevenueReport, Long> {
    List<RevenueReport> findByDateBetweenOrderByDateDesc(Instant startDate, Instant endDate);

    List<RevenueReport> findAllByOrderByDateDesc();
    
    @Query("SELECT r FROM RevenueReport r WHERE DATE(r.date) = DATE(:date)")
    Optional<RevenueReport> findByDate(@Param("date") Instant date);

    @Query("SELECT r FROM RevenueReport r WHERE r.date >= :startDate AND r.date <= :endDate ORDER BY r.date DESC")
    List<RevenueReport> findRevenueReportsByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.quantityRentalOrder), 0) FROM RevenueReport r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumQuantityRentalOrdersByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalLateFee), 0) FROM RevenueReport r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalLateFeeByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalRentalPrice), 0) FROM RevenueReport r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalRentalByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalDeposit), 0) FROM RevenueReport r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalDepositByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("SELECT COALESCE(SUM(r.totalRevenue), 0) FROM RevenueReport r WHERE r.date >= :startDate AND r.date <= :endDate")
    Long sumTotalRevenueByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

}