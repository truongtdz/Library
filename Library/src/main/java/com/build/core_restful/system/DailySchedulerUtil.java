package com.build.core_restful.system;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.build.core_restful.service.SubscribeService;
import com.build.core_restful.service.TrainService;

@Component
public class DailySchedulerUtil {
    private final SubscribeService subscribeService;
    private final TrainService trainService;

    public DailySchedulerUtil(
        SubscribeService subscribeService,
        TrainService trainService
    ){
        this.subscribeService = subscribeService;
        this.trainService = trainService;
    };

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void trainDataScheduler() {
        trainService.trainDataEveryDay();
    }
    
    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendEmailScheduler() {
        subscribeService.sendEmailToUser();
    }
}
