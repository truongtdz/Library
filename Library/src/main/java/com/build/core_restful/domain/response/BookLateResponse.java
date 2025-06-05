package com.build.core_restful.domain.response;

import java.time.Instant;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookLateResponse {
    private String email;
    private String bookName;
    private String imageUrl;
    private String rentalOrderId;
    private Instant rentalDate;
    private Instant rentedDate;
}
