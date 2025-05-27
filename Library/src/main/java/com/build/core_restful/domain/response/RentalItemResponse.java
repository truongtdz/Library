package com.build.core_restful.domain.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalItemResponse {
    private Long id;
    private Instant rentalDate;
    private Instant returnDate;

    private String bookName;
    private Long rentalPrice;
    private Long depositPrice;
    private Long lateFee;
    private Long quantity;
    private Long totalRental;
    private Long totalDeposit;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
