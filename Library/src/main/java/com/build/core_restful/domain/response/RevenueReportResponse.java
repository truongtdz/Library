package com.build.core_restful.domain.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueReportResponse {
    private Long id;
    private Instant date;
    private Long quantityRentalOrder;
    private Long totalLateFee;
    private Long totalRental;
    private Long totalDeposit;
    private Long totalRevenue;
    private BigDecimal dailyGrowthPercentage;
    private Long dailyGrowthAmount;
    private BigDecimal weeklyGrowthPercentage;
    private Long weeklyGrowthAmount;
    private BigDecimal monthlyGrowthPercentage;
    private Long monthlyGrowthAmount;
    private Instant createdAt;
    private Instant updatedAt;
}