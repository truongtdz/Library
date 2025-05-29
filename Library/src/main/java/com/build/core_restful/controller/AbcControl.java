package com.build.core_restful.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.service.SubscribeService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class AbcControl {
    private final SubscribeService service;

    @GetMapping()
    public String getMethodName() {
        service.sendEmailToUser();
        return "Demo send email";
    }
    
}
