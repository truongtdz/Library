package com.build.core_restful.controller;

import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentalOrderResponse;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.OrderStatusEnum;
import com.build.core_restful.util.enums.PaymentStatusEnum;

import jakarta.validation.Valid;

import java.time.Instant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/order/rental")
public class RentalOrderController {
    private final RentalOrderService rentalOrderService;

    public RentalOrderController(RentalOrderService rentalOrderService) {
        this.rentalOrderService = rentalOrderService;
    }

    @GetMapping("/all")
    @AddMessage("Get all orders")
    public ResponseEntity<PageResponse<Object>> getAll(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String orderStatus,
        @RequestParam(required = false) Long fromTotalPrice,
        @RequestParam(required = false) Long toTotalPrice,
        @RequestParam(required = false) Long fromDepositPrice,
        @RequestParam(required = false) Long toDepositPrice, 
        @RequestParam(required = false) Long userId 
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(rentalOrderService.getAllOrder(
            fromTotalPrice, toTotalPrice, fromDepositPrice, toDepositPrice, userId, orderStatus, pageable
        ));
    }
    
    @GetMapping("/by/{id}")
    @AddMessage("Get order by id")
    public ResponseEntity<RentalOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalOrderService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody RentalOrderRequest request){
        return ResponseEntity.ok(rentalOrderService.create(request));
    }

    @PutMapping("/update/confirm/{id}")
    public ResponseEntity<Boolean> confirmRentalOrder(@PathVariable Long id) {
        return ResponseEntity.ok(rentalOrderService.confirmOrder(id));
    }   

    @PutMapping("/update/cancel/{id}")
    public ResponseEntity<Boolean> cancelRentalOrder(@PathVariable Long id) {
        return ResponseEntity.ok(rentalOrderService.cancelOrder(id));
    }

    @PutMapping("/update/status/order/{id}")
    @AddMessage("Update status order")
    public ResponseEntity<RentalOrderResponse> updateOrderStatus(@PathVariable Long id, @Valid @RequestParam OrderStatusEnum newStatus) {
        return ResponseEntity.ok(rentalOrderService.updateOrderStatus(id, newStatus));
    }

    @PutMapping("/update/status/payment/{id}")
    @AddMessage("Update status order")
    public ResponseEntity<RentalOrderResponse> updatePaymentStatus(@PathVariable Long id, @Valid @RequestParam PaymentStatusEnum newStatus) {
        return ResponseEntity.ok(rentalOrderService.updatePaymentStatus(id, newStatus));
    }

    @GetMapping("/total/quantity")
    public ResponseEntity<Long> getQuantityByOrderStatus(
        @RequestParam(required = false) Instant date
    ){
        return ResponseEntity.ok(rentalOrderService.getQuantityByOrderStatus(date));
    }

    @GetMapping("/total/revenue")
    public ResponseEntity<Long> getRevenueRentalOrder(
        @RequestParam(required = false) Instant date
    ){
        return ResponseEntity.ok(rentalOrderService.getRevenueRentalOrder(date));
    }

    @GetMapping("/total/deposit")
    public ResponseEntity<Long> getTotalDepositOrder(
        @RequestParam(required = false) Instant date
    ){
        return ResponseEntity.ok(rentalOrderService.getTotalDepositOrder(date));
    }
}
