package com.build.core_restful.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.service.SubscribeService;
import com.build.core_restful.service.TrainService;
import com.build.core_restful.domain.response.BookSendEmailResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/cron")
@RequiredArgsConstructor
public class TestCron {
    private final TrainService trainService;
    private final SubscribeService subscribeService;

    @GetMapping("/email")
    public String sendEmail() {
        subscribeService.sendEmailToUser();
        return "Demo send email";
    }

    @GetMapping("/train")
    public List<BookSendEmailResponse> train() {
        return trainService.predictCategory(21L, "Female", "Hanoi");
    }
    
}
