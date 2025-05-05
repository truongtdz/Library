package com.build.core_restful.controller.order;

import com.build.core_restful.domain.request.RentalOrderRequest;
import com.build.core_restful.service.RentalOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {
    private final RentalOrderService rentalOrderService;

    public PaymentController(RentalOrderService rentalOrderService) {
        this.rentalOrderService = rentalOrderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Object> checkoutOneProduct(@RequestBody RentalOrderRequest request){
        return ResponseEntity.ok(rentalOrderService.create(request));
    }


}
