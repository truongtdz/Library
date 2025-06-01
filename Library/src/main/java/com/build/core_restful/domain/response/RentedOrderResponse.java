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

    private Long userId;

    private List<RentalItemRes> items;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RentalItemRes {
        private Long itemId;
        private Long orderId;
    }
}
