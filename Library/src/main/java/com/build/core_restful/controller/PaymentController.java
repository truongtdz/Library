package com.build.core_restful.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.domain.Payment;
import com.build.core_restful.domain.request.PaymentRequest;
import com.build.core_restful.domain.response.PaymentResponse;
import com.build.core_restful.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(
        PaymentService paymentService
    ){
        this.paymentService = paymentService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestParam Map<String, String> params) {
        PaymentResponse response = paymentService.handleCallback(params);
        return ResponseEntity.ok("RspCode=" + ("SUCCESS".equals(response.getStatus()) ? "00" : "01"));
    }
    
    @GetMapping("/status/{orderId}")
    public ResponseEntity<Payment> getPaymentStatus(@PathVariable String orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        if (payment != null) {
            return ResponseEntity.ok(payment);
        }
        return ResponseEntity.notFound().build();
    }
}
