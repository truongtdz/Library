package com.build.core_restful.domain.request;

import java.time.Instant;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRevenueByLateFee {
    private Instant date;
    private Long totalFee;
    private Long totalRentalPrice;
}
