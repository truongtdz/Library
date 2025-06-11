package com.build.core_restful.domain.response;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentedOrderResponse {
    private Long id;

    private String city;
    private String district;
    private String ward;
    private String street;
    private String notes;
    private String fullName;
    private String phone;

    private String deliveryMethod;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;

    private Instant receiveDay;
    private Instant returnDate;

    private Long userId;

    private List<RentalItemResponse> items;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
