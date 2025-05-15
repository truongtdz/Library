package com.build.core_restful.controller;

import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentalOrderResponse;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.OrderStatusEnum;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class RentalOrderController {
    private final RentalOrderService rentalOrderService;

    public RentalOrderController(RentalOrderService rentalOrderService) {
        this.rentalOrderService = rentalOrderService;
    }

    @PutMapping("/{id}")
    @AddMessage("Update status order")
    public ResponseEntity<RentalOrderResponse> update(@PathVariable Long id, @Valid @RequestBody OrderStatusEnum newStatus) {
        return ResponseEntity.ok(rentalOrderService.update(id, newStatus));
    }

    @GetMapping("/{id}")
    @AddMessage("Get order by id")
    public ResponseEntity<RentalOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentalOrderService.getById(id));
    }

    @GetMapping
    @AddMessage("Get all orders")
    public ResponseEntity<PageResponse<Object>> getAll(@RequestParam int page,
                                               @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(rentalOrderService.getAll(pageable));
    }
}
