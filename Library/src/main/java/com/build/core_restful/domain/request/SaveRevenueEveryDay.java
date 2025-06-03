package com.build.core_restful.domain.request;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveRevenueEveryDay {
    private Instant date;
    private Long totalRentalPrice;
    private Long totalDeposit;
    private Long totalRevenue;
}
