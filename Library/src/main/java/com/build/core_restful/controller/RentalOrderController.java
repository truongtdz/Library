package com.build.core_restful.controller;

import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentalOrderResponse;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.OrderStatusEnum;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/order")
public class RentalOrderController {
    private final RentalOrderService rentalOrderService;

    public RentalOrderController(RentalOrderService rentalOrderService) {
        this.rentalOrderService = rentalOrderService;
    }

    @PostMapping()
    public ResponseEntity<Object> createOrder(@RequestBody RentalOrderRequest request){
        return ResponseEntity.ok(rentalOrderService.create(request));
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<RentalOrderResponse> confirmRentalOrder(@PathVariable Long id) {
        return ResponseEntity.ok(rentalOrderService.update(id, OrderStatusEnum.Confirmed));
    }   

    @PutMapping("/cancel/{id}")
    public ResponseEntity<RentalOrderResponse> cancelRentalOrder(@PathVariable Long id) {
        return ResponseEntity.ok(rentalOrderService.update(id, OrderStatusEnum.Cancelled));
    }

    @PutMapping("/{id}")
    @AddMessage("Update status order")
    public ResponseEntity<RentalOrderResponse> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusEnum newStatus) {
        return ResponseEntity.ok(rentalOrderService.update(id, newStatus));
    }

    @GetMapping("/{id}")
    @AddMessage("Get order by id")
    public ResponseEntity<RentalOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalOrderService.getById(id));
    }

    @GetMapping
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
}
