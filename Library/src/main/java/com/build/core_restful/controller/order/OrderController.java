package com.build.core_restful.controller.order;

import com.build.core_restful.domain.request.OrderRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.OrderService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.exception.NewException;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @AddMessage("Get all orders")
    public ResponseEntity<PageResponse<Object>> getAllOrders(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);

        PageResponse<Object> orders = orderService.getAllOrders(pageable);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @AddMessage("Get order by id")
    public ResponseEntity<Object> getOrderById(@PathVariable Long id) {
        if (!orderService.existOrderById(id)) {
            throw new NewException("Order id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    @AddMessage("Create new order")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrderRequest newOrder) {
        return ResponseEntity.ok(orderService.createOrder(newOrder));
    }

    @PutMapping("/{id}")
    @AddMessage("Update order")
    public ResponseEntity<Object> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest updateOrder) {
        if (!orderService.existOrderById(id)) {
            throw new NewException("Order id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(orderService.updateOrder(updateOrder));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete order")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderService.existOrderById(id)) {
            throw new NewException("Order id = " + id + " không tồn tại");
        }
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}
