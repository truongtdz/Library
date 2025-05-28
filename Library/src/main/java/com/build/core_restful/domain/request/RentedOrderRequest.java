package com.build.core_restful.domain.request;

import java.util.List;

import com.build.core_restful.util.enums.PaymentMethodEnum;
import com.build.core_restful.util.enums.PaymentStatusEnum;
import com.build.core_restful.util.enums.ShippingMethodEnum;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentedOrderRequest {
    @NotNull
    private Long userId;

    private String city;
    private String district;
    private String ward;
    private String street;
    private String notes;

    private PaymentStatusEnum paymentStatus;
    private PaymentMethodEnum paymentMethod;
    private ShippingMethodEnum shippingMethod;

    private List<Long> itemIdLists;
}
