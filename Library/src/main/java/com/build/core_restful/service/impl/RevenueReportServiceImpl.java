package com.build.core_restful.service.impl;


import com.build.core_restful.domain.RevenueReports;
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
    public List<RevenueReportResponse> getAllRevenueReports() {
        List<RevenueReports> reports = revenueReportRepository.findAllByOrderByDateDesc();
        return revenueReportMapper.toRevenueReportResponseList(reports);
    }

    @Override
    public List<RevenueReportResponse> getRevenueReportsByDateRange(Instant startDate, Instant endDate) {
        List<RevenueReports> reports = revenueReportRepository.findByDateBetweenOrderByDateDesc(startDate, endDate);
        return revenueReportMapper.toRevenueReportResponseList(reports);
    }

    @Override
    public TotalFieldInRevenueResponse getQuantityRentalOrders(Instant startDate, Instant endDate) {
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

}