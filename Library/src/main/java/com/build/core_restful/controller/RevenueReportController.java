package com.build.core_restful.controller;

import com.build.core_restful.domain.response.RevenueReportResponse;
import com.build.core_restful.domain.response.TotalFieldInRevenueResponse;
import com.build.core_restful.service.RevenueReportService;
import com.build.core_restful.util.annotation.AddMessage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/revenue")
public class RevenueReportController {
    private final RevenueReportService revenueReportService;

    public RevenueReportController(
        RevenueReportService revenueReportService
    ){
        this.revenueReportService = revenueReportService;
    };

    @GetMapping("/all")
    @AddMessage("Get all revenue report")
    public ResponseEntity<List<RevenueReportResponse>> getAllRevenueReports() {
        return ResponseEntity.ok(revenueReportService.getAllRevenueReport());
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get revenue report by id")
    public ResponseEntity<Object> getRevenueReportById(@PathVariable Long id) {
        return ResponseEntity.ok(revenueReportService.getRevenueReportById(id));
    }

    @GetMapping("/by/date")
    @AddMessage("Get revenue report by date range")
    public ResponseEntity<List<RevenueReportResponse>> getRevenueReportsByDateRange(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return ResponseEntity.ok(revenueReportService.getRevenueReportByDateRange(startDate, endDate));
    }

    @GetMapping("/total/rental-orders")
    @AddMessage("Get total quantity of rental orders")
    public ResponseEntity<TotalFieldInRevenueResponse> getQuantityRentalOrders(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return ResponseEntity.ok(revenueReportService.getQuantityRentalOrder(startDate, endDate));
    }

    @GetMapping("/total/late-fee")
    @AddMessage("Get total late fee")
    public ResponseEntity<TotalFieldInRevenueResponse> getTotalLateFee(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return ResponseEntity.ok(revenueReportService.getTotalLateFee(startDate, endDate));
    }

    @GetMapping("/total/rental-price")
    @AddMessage("Get total rental amount")
    public ResponseEntity<TotalFieldInRevenueResponse> getTotalRental(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return ResponseEntity.ok(revenueReportService.getTotalRentalPrice(startDate, endDate));
    }

    @GetMapping("/total/deposit")
    @AddMessage("Get total deposit amount")
    public ResponseEntity<TotalFieldInRevenueResponse> getTotalDeposit(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return ResponseEntity.ok(revenueReportService.getTotalDeposit(startDate, endDate));
    }

    @GetMapping("/total/revenue")
    @AddMessage("Get total revenue")
    public ResponseEntity<TotalFieldInRevenueResponse> getTotalRevenue(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return ResponseEntity.ok(revenueReportService.getTotalRevenue(startDate, endDate));
    }
}
