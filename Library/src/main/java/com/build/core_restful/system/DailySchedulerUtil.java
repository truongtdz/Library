package com.build.core_restful.system;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.build.core_restful.domain.request.SaveRevenueEveryDay;
import com.build.core_restful.service.RentalItemService;
import com.build.core_restful.service.RentalOrderService;
import com.build.core_restful.service.RevenueReportService;
import com.build.core_restful.service.SubscribeService;
import com.build.core_restful.service.TrainService;

@Component
public class DailySchedulerUtil {
    private final SubscribeService subscribeService;
    private final TrainService trainService;
    private final RevenueReportService revenueReportService;
    private final RentalOrderService rentalOrderService;
    private final RentalItemService rentalItemService;

    public DailySchedulerUtil(
        SubscribeService subscribeService,
        TrainService trainService,
        RevenueReportService revenueReportService,
        RentalOrderService rentalOrderService,
        RentalItemService rentalItemService
    ){
        this.subscribeService = subscribeService;
        this.trainService = trainService;
        this.revenueReportService = revenueReportService;
        this.rentalOrderService = rentalOrderService;
        this.rentalItemService = rentalItemService;
    };

    @Scheduled(cron = "0 55 23 * * *", zone = "Asia/Ho_Chi_Minh")
    public void trainDataScheduler() {
        trainService.trainDataEveryDay();
        
        SaveRevenueEveryDay data = rentalOrderService.getRevenueEveryDay();
        revenueReportService.saveRevenueDate(data);
    }
    
    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Ho_Chi_Minh")
    public void sendEmailScheduler() {
        subscribeService.sendEmailRecommendBook();

        rentalItemService.sendEmailLateBook();
    }
}
