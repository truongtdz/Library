package com.build.core_restful.domain.request;

import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueReportRequest {
    private Long id;
    private Instant date;
    private Long quantityRentalOrder;
    private Long totalLateFee;
    private Long totalRental;
    private Long totalDeposit;
    private Long totalRevenue;
}