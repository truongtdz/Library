package com.build.core_restful.util.system;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.build.core_restful.service.SubscribeService;

@Component
public class DailySchedulerUtil {
    private final SubscribeService subscribeService;

    public DailySchedulerUtil(
        SubscribeService subscribeService
    ){
        this.subscribeService = subscribeService;
    };
    
    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendEmailScheduler() {
        subscribeService.sendEmailToUser();
    }
}
