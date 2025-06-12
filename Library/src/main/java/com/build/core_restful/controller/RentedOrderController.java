package com.build.core_restful.controller;

import com.build.core_restful.domain.request.RentedOrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RentedOrderResponse;
import com.build.core_restful.service.RentedOrderService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.OrderStatusEnum;

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

    @GetMapping("/all")
    @AddMessage("Get all orders")
    public ResponseEntity<PageResponse<Object>> getAll(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) OrderStatusEnum orderStatus,
        @RequestParam(required = false) Long fromLateFee,
        @RequestParam(required = false) Long toLateFee,
        @RequestParam(required = false) Long userId 
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        String status = null; Long id = null;
        if(orderStatus != null){
            status = orderStatus.toString();
        }
        if(userId != null){
            id = userId;
        }

        return ResponseEntity.ok(rentedOrderService.getAllRentedOrder(
            fromLateFee, toLateFee, id, status, pageable
        ));
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get order by id")
    public ResponseEntity<RentedOrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rentedOrderService.getById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody RentedOrderRequest request){
        return ResponseEntity.ok(rentedOrderService.create(request));
    }

    @PutMapping("/update/{id}")
    @AddMessage("Update status order")
    public ResponseEntity<RentedOrderResponse> updateOrderStatus(@PathVariable Long id,@RequestParam OrderStatusEnum newStatus) {
        return ResponseEntity.ok(rentedOrderService.updateStatus(id, newStatus));
    }

    @PutMapping("/update/note/{id}")
    @AddMessage("Update status order")
    public ResponseEntity<RentedOrderResponse> updateNotes(@PathVariable Long id,@RequestParam String newNote) {
        return ResponseEntity.ok(rentedOrderService.updateNote(id, newNote));
    }
    
    @PutMapping("/update/confirm/{id}")
    public ResponseEntity<Boolean> confirmRentalOrder(@PathVariable Long id) {
        return ResponseEntity.ok(rentedOrderService.confirmOrder(id));
    }   

    @PutMapping("/update/cancel/{id}")
    public ResponseEntity<Boolean> cancelRentalOrder(@PathVariable Long id) {
        return ResponseEntity.ok(rentedOrderService.cancelOrder(id));
    }
}