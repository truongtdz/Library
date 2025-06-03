package com.build.core_restful.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.request.ChangeRevenueByLateFee;
import com.build.core_restful.domain.request.SaveRevenueEveryDay;
import com.build.core_restful.domain.response.RevenueReportResponse;
import com.build.core_restful.domain.response.TotalFieldInRevenueResponse;

@Service
public interface RevenueReportService {
    RevenueReportResponse getRevenueReportById(Long id);
    
    List<RevenueReportResponse> getAllRevenueReport();
    
    List<RevenueReportResponse> getRevenueReportByDateRange(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getQuantityRentalOrder(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalLateFee(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalRental(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalDeposit(Instant startDate, Instant endDate);

    TotalFieldInRevenueResponse getTotalRevenue(Instant startDate, Instant endDate);

    void saveRevenueDate(SaveRevenueEveryDay saveRevenueEveryDay);
 
    void changeLateFeeAndRevenue(ChangeRevenueByLateFee changeRevenueByLateFee);
}
