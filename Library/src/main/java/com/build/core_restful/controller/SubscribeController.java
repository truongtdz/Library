package com.build.core_restful.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.domain.request.SubscribeRequest;
import com.build.core_restful.service.SubscribeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/subscribe")
public class SubscribeController {
    private final SubscribeService subscribeService;

    public SubscribeController(
        SubscribeService subscribeService
    ){
        this.subscribeService = subscribeService;
    };

    @GetMapping("/check")
    public ResponseEntity<Boolean> getStatusSubscribe(@RequestParam String email) {
        return ResponseEntity.ok(subscribeService.existsByEmail(email));
    }
    

    @PostMapping("/create")
    public ResponseEntity<Boolean> subscribe(@RequestBody SubscribeRequest request) {
        return ResponseEntity.ok(subscribeService.createSubscribe(request));
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteSubscribe(@RequestBody SubscribeRequest request) {
        return ResponseEntity.ok(subscribeService.deleteSubscribe(request.getEmail()));
    }
}
