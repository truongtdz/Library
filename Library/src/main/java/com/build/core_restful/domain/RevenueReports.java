package com.build.core_restful.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.build.core_restful.util.system.JwtUtil;

@Entity
@Table(name = "revenue_reports")
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
    private Long lateFees;
    private Long rentals;
    private Long totalRevenue;

    private Instant updateAt;
    private String updateBy;

    @PreUpdate
    public void handleBeforeUpdate() {
        this.setUpdateBy(JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "");

        this.setUpdateAt(Instant.now());
    }
}
