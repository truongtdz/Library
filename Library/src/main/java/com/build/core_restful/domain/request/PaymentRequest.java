package com.build.core_restful.domain.request;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String returnUrl;
    private String cancelUrl;
    private String description;
}
