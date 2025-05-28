package com.build.core_restful.controller;

import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentedOrderResponse;
import com.build.core_restful.service.RentedOrderService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.OrderStatusEnum;

import jakarta.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/order/rented")
public class RentedOrderController {
    private final RentedOrderService rentedOrderService;

    public RentedOrderController(RentedOrderService rentedOrderService) {
        this.rentedOrderService = rentedOrderService;
    }

    @PostMapping()
    public ResponseEntity<Object> createOrder(@RequestBody RentedOrderRequest request){
        return ResponseEntity.ok(rentedOrderService.create(request));
    }

    @PutMapping("/{id}")
    @AddMessage("Update status order")
    public ResponseEntity<RentedOrderResponse> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusEnum newStatus) {
        return ResponseEntity.ok(rentedOrderService.update(id, newStatus));
    }

    @GetMapping("/{id}")
    @AddMessage("Get order by id")
    public ResponseEntity<RentedOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentedOrderService.getById(id));
    }

    @GetMapping
    @AddMessage("Get all orders")
    public ResponseEntity<PageResponse<Object>> getAll(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String orderStatus,
        @RequestParam(required = false) Long fromLateFee,
        @RequestParam(required = false) Long toLateFee,
        @RequestParam(required = false) Long userId 
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(rentedOrderService.getAllRentedOrder(
            fromLateFee, toLateFee, userId, orderStatus, pageable
        ));
    }

}