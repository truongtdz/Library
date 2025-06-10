package com.build.core_restful.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.RentalItemService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.OrderStatusEnum;

@RestController
@RequestMapping("/api/v1/item/rental")
public class RentalItemController {
    private final RentalItemService rentalItemService;

    public RentalItemController(
        RentalItemService rentalItemService
    ){
        this.rentalItemService = rentalItemService;
    }

    @GetMapping("/all")
    @AddMessage("Get items by user")
    public ResponseEntity<PageResponse<Object>> getItemByUser(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) OrderStatusEnum itemStatus,
        @RequestParam(required = false) Long userId 
    ) {
        Pageable pageable = PageRequest.of(page, size);

        String status = null; Long id = null;
        if(itemStatus != null){
            status = itemStatus.toString();
        }
        if(userId != null){
            id = userId;
        }

        return ResponseEntity.ok(rentalItemService.getItemByUser(
            status, id, pageable
        ));
    }
}
