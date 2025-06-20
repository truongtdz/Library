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
    private Long rentalOrderId;
    private Long timeRental;

    private Instant rentalDate;
    private Instant receiveDate;
    private Instant returnDate;

    private String bookName;
    private String imageUrl;
    private Long rentalPrice;
    private Long depositPrice;
    private Long quantity;
    private Long totalRental;
    private Long totalDeposit;

    private String status;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
