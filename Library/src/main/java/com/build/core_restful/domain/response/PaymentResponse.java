package com.build.core_restful.domain.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String paymentUrl;
    private String orderId;
    private String status;
    private String message;
}
