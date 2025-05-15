package com.build.core_restful.controller;

import com.build.core_restful.domain.request.CartRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.CartService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    @AddMessage("Get book at cart by user")
    public ResponseEntity<PageResponse<Object>> getCartByUser(
            @PathVariable Long id,
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(cartService.getByUser(id, pageable));
    }

    @PostMapping()
    @AddMessage("Add book to cart")
    public ResponseEntity<Boolean> getCartByUser(
            @Valid @RequestBody CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.addBookToCart(cartRequest));
    }

    @DeleteMapping()
    @AddMessage("Delete book at cart")
    public ResponseEntity<Object> deleteBookAtCart(
            @Valid @RequestBody CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.deleteBookAtCart(cartRequest));
    }
}
