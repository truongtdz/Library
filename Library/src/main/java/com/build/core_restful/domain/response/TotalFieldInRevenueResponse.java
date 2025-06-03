package com.build.core_restful.domain.response;

import java.time.Instant;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalFieldInRevenueResponse {
    private Instant startDate;
    private Instant endDate;
    private Long totalValue;
}
