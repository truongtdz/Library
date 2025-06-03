package com.build.core_restful.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.response.RevenueReportResponse;
import com.build.core_restful.domain.response.TotalFieldInRevenueResponse;

@Service
public interface RevenueReportService {
    RevenueReportResponse getRevenueReportById(Long id);
    
    List<RevenueReportResponse> getAllRevenueReports();
    
    List<RevenueReportResponse> getRevenueReportsByDateRange(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getQuantityRentalOrders(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalLateFee(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalRental(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalDeposit(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalRevenue(Instant startDate, Instant endDate);
 
}
