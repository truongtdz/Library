package com.build.core_restful.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.build.core_restful.domain.Payment;
import com.build.core_restful.domain.request.PaymentRequest;
import com.build.core_restful.domain.response.PaymentResponse;
import com.build.core_restful.repository.PaymentRepository;
import com.build.core_restful.service.PaymentService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(
        PaymentRepository paymentRepository
    ){
        this.paymentRepository = paymentRepository;
    }
    
    @Value("${vnpay.pay-url}")
    private String vnpayUrl;
    
    @Value("${vnpay.tmn-code}")
    private String tmnCode;
    
    @Value("${vnpay.secret-key}")
    private String secretKey;
    
    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        // Thêm validation
        if (request.getAmount().compareTo(BigDecimal.valueOf(5000)) < 0) {
            return new PaymentResponse(null, request.getOrderId(), "ERROR", 
                "Amount must be at least 5000 VND");
        }
        
        if (request.getOrderId() == null || request.getOrderId().trim().isEmpty()) {
            return new PaymentResponse(null, null, "ERROR", "OrderId is required");
        }
        
        try {
            // 1. Tạo payment record
            Payment payment = new Payment();
            payment.setOrderId(request.getOrderId());
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            payment.setPaymentMethod("VNPAY");
            
            paymentRepository.save(payment);
            
            // 2. Tạo URL thanh toán VNPay
            String paymentUrl = createVNPayUrl(request);
            
            return new PaymentResponse(paymentUrl, request.getOrderId(), "PENDING", "Payment URL created successfully");
            
        } catch (Exception e) {
            return new PaymentResponse(null, request.getOrderId(), "ERROR", e.getMessage());
        }
    }
    
    private String createVNPayUrl(PaymentRequest request) throws Exception {
        Map<String, String> params = new TreeMap<>();
        
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", String.valueOf(request.getAmount().multiply(new BigDecimal(100)).intValue()));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", request.getOrderId());
        params.put("vnp_OrderInfo", request.getDescription());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", request.getReturnUrl());
        params.put("vnp_IpAddr", "127.0.0.1");
        
        // ✅ SỬA: Format datetime đúng theo VNPay
        params.put("vnp_CreateDate", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        
        // Tạo hash data
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (hashData.length() > 0) {
                hashData.append('&');
                query.append('&');
            }
            hashData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        
        // Tạo secure hash
        String vnpSecureHash = hmacSHA512(secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);
        
        // Debug log
        System.out.println("Hash data: " + hashData.toString());
        System.out.println("Final URL: " + vnpayUrl + "?" + query.toString());
        
        return vnpayUrl + "?" + query.toString();
    }
    
    @Override
    public PaymentResponse handleCallback(Map<String, String> params) {
        try {
            String orderId = params.get("vnp_TxnRef");
            String responseCode = params.get("vnp_ResponseCode");
            String transactionId = params.get("vnp_TransactionNo");
            
            // Verify hash
            if (!verifyCallback(params)) {
                return new PaymentResponse(null, orderId, "FAILED", "Invalid signature");
            }
            
            // Cập nhật payment status
            Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);
            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();
                payment.setTransactionId(transactionId);
                payment.setUpdateAt(Instant.now());
                
                if ("00".equals(responseCode)) {
                    payment.setStatus("SUCCESS");
                } else {
                    payment.setStatus("FAILED");
                }
                
                paymentRepository.save(payment);
                
                return new PaymentResponse(null, orderId, payment.getStatus(), "Payment processed successfully");
            }
            
            return new PaymentResponse(null, orderId, "FAILED", "Payment not found");
            
        } catch (Exception e) {
            return new PaymentResponse(null, "", "ERROR", e.getMessage());
        }
    }
    
    private boolean verifyCallback(Map<String, String> params) {
        String vnpSecureHash = params.get("vnp_SecureHash");
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : new TreeMap<>(params).entrySet()) {
            if (hashData.length() > 0) {
                hashData.append('&');
            }
            hashData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        
        String calculatedHash = hmacSHA512(secretKey, hashData.toString());
        return calculatedHash.equals(vnpSecureHash);
    }
    
    private String hmacSHA512(String key, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC", e);
        }
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    @Override
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId).orElse(null);
    }
}