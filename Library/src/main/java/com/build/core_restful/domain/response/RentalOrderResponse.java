package com.build.core_restful.domain.response;

import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.enums.PaymentMethodEnum;
import com.build.core_restful.util.enums.PaymentStatusEnum;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOrderResponse {
    private Long id;

    private Long totalPrice;
    private Long depositPrice;

    private String city;
    private String district;
    private String ward;
    private String street;
    private String notes;

    private OrderStatusEnum orderStatus;
    private PaymentStatusEnum paymentStatus;
    private PaymentMethodEnum paymentMethod;

    private Long userId;

    private List<RentalItemResponse> items;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
