package com.build.core_restful.service.impl;


import com.build.core_restful.domain.RevenueReport;
import com.build.core_restful.domain.request.ChangeRevenueByLateFee;
import com.build.core_restful.domain.request.SaveRevenueEveryDay;
import com.build.core_restful.domain.response.RevenueReportResponse;
import com.build.core_restful.domain.response.TotalFieldInRevenueResponse;
import com.build.core_restful.repository.RevenueReportRepository;
import com.build.core_restful.service.RevenueReportService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.RevenueReportMapper;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RevenueReportServiceImpl implements RevenueReportService {
    private final RevenueReportRepository revenueReportRepository;
    private final RevenueReportMapper revenueReportMapper;

    public RevenueReportServiceImpl(
        RevenueReportRepository revenueReportRepository,
        RevenueReportMapper revenueReportMapper
    ){
        this.revenueReportRepository = revenueReportRepository;
        this.revenueReportMapper = revenueReportMapper;
    };

    @Override
    public RevenueReportResponse getRevenueReportById(Long id) {
        return revenueReportMapper.toRevenueReportResponse(
            revenueReportRepository.findById(id)
                .orElseThrow(() -> new NewException("Revenue report with id: " + id + " not exist!"))
        );
    }

    @Override
    public List<RevenueReportResponse> getAllRevenueReport() {
        List<RevenueReport> reports = revenueReportRepository.findAllByOrderByDateDesc();
        return revenueReportMapper.toRevenueReportResponseList(reports);
    }

    @Override
    public List<RevenueReportResponse> getRevenueReportByDateRange(Instant startDate, Instant endDate) {
        List<RevenueReport> reports = revenueReportRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
        return revenueReportMapper.toRevenueReportResponseList(reports);
    }

    @Override
    public TotalFieldInRevenueResponse getQuantityRentalOrder(Instant startDate, Instant endDate) {
        return TotalFieldInRevenueResponse.builder()
            .startDate(startDate)
            .endDate(endDate)
            .totalValue(revenueReportRepository.sumQuantityRentalOrdersByDateRange(startDate, endDate))
            .build();
            
    }

    @Override
    public TotalFieldInRevenueResponse getTotalLateFee(Instant startDate, Instant endDate) {
        return TotalFieldInRevenueResponse.builder()
            .startDate(startDate)
            .endDate(endDate)
            .totalValue(revenueReportRepository.sumTotalLateFeeByDateRange(startDate, endDate))
            .build();
            
    }

    @Override
    public TotalFieldInRevenueResponse getTotalRental(Instant startDate, Instant endDate) {
        return TotalFieldInRevenueResponse.builder()
            .startDate(startDate)
            .endDate(endDate)
            .totalValue(revenueReportRepository.sumTotalRentalByDateRange(startDate, endDate))
            .build();
            
    }

    @Override
    public TotalFieldInRevenueResponse getTotalDeposit(Instant startDate, Instant endDate) {
        return TotalFieldInRevenueResponse.builder()
            .startDate(startDate)
            .endDate(endDate)
            .totalValue(revenueReportRepository.sumTotalDepositByDateRange(startDate, endDate))
            .build();
            
    }

    @Override
    public TotalFieldInRevenueResponse getTotalRevenue(Instant startDate, Instant endDate) {
        return TotalFieldInRevenueResponse.builder()
            .startDate(startDate)
            .endDate(endDate)
            .totalValue(revenueReportRepository.sumTotalRevenueByDateRange(startDate, endDate))
            .build();
            
    }

    @Override
    public void saveRevenueDate(SaveRevenueEveryDay saveRevenueEveryDay) {
        RevenueReport revenueReport = RevenueReport.builder()
                                        .date(Instant.now())
                                        .totalLateFee(0L)
                                        .totalRentalPrice(saveRevenueEveryDay.getTotalRentalPrice())
                                        .totalDeposit(saveRevenueEveryDay.getTotalDeposit())
                                        .totalRevenue(saveRevenueEveryDay.getTotalRevenue())
                                        .build();

        revenueReportRepository.save(revenueReport);
    }

    @Override
    public void changeLateFeeAndRevenue(ChangeRevenueByLateFee changeRevenueByLateFee){
        RevenueReport curRevenueReport = revenueReportRepository.findByDate(changeRevenueByLateFee.getDate())
                                            .orElseThrow(() -> new NewException("RevenueReport with date: " + changeRevenueByLateFee.getDate() + " not found"));
        
        curRevenueReport.setTotalLateFee(curRevenueReport.getTotalLateFee());
        curRevenueReport.setTotalRevenue(curRevenueReport.getTotalRevenue() + changeRevenueByLateFee.getTotalFee());
        curRevenueReport.setTotalDeposit(curRevenueReport.getTotalDeposit() - changeRevenueByLateFee.getTotalFee());

        revenueReportRepository.save(curRevenueReport);
    }

}