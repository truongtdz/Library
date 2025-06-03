package com.build.core_restful.domain;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.build.core_restful.system.JwtUtil;

@Entity
@Table(name = "revenue_reports", indexes = {
    @Index(name = "idx_date", columnList = "date")
})
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant date;

    private Long quantityRentalOrder;

    private Long totalLateFee;
    private Long totalRental;
    private Long totalDeposit;

    private Long totalRevenue;

    @Column(name = "daily_growth_percentage", precision = 5, scale = 2)
    private BigDecimal dailyGrowthPercentage;
    
    @Column(name = "daily_growth_amount")
    private Long dailyGrowthAmount;

    @Column(name = "weekly_growth_percentage", precision = 5, scale = 2)
    private BigDecimal weeklyGrowthPercentage;
    
    @Column(name = "weekly_growth_amount")
    private Long weeklyGrowthAmount;

    @Column(name = "monthly_growth_percentage", precision = 5, scale = 2)
    private BigDecimal monthlyGrowthPercentage;
    
    @Column(name = "monthly_growth_amount")
    private Long monthlyGrowthAmount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
