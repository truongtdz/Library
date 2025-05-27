package com.build.core_restful.domain.request;

import com.build.core_restful.util.enums.PaymentMethodEnum;
import com.build.core_restful.util.enums.PaymentStatusEnum;
import com.build.core_restful.util.enums.ShippingMethod;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOrderRequest {
    @NotNull
    private Long userId;

    private String city;
    private String district;
    private String ward;
    private String street;
    private String notes;

    private PaymentStatusEnum paymentStatus;
    private PaymentMethodEnum paymentMethod;
    private ShippingMethod shippingMethod;

    private List<RentalItemRequest> items;
}
