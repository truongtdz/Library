package com.build.core_restful.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Payment;
import com.build.core_restful.domain.request.PaymentRequest;
import com.build.core_restful.domain.response.PaymentResponse;

@Service
public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse handleCallback(Map<String, String> params);

    Payment getPaymentByOrderId(String orderId);  
}
