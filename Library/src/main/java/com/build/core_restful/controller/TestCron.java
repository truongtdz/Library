package com.build.core_restful.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.build.core_restful.service.RentalItemService;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.service.SubscribeService;
import com.build.core_restful.service.TrainService;
import com.build.core_restful.domain.response.BookRecommendResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/cron")
@RequiredArgsConstructor
public class TestCron {
    private final TrainService trainService;
    private final SubscribeService subscribeService;
    private final RentalItemService rentalItemService;

    @GetMapping("/email/recommend")
    public String sendEmailRecommend() {
        subscribeService.sendEmailRecommendBook();
        return "Demo send email recommend";
    }

    @GetMapping("/email/late")
    public String sendEmailLate() {
        rentalItemService.sendEmailLateBook();
        return "Demo send email";
    }

    @GetMapping("/train")
    public List<BookRecommendResponse> train() {
        return trainService.predictCategory(21L, "Female", "Hanoi");
    }
    
}
