package com.build.core_restful.domain;


import com.build.core_restful.util.JwtUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

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
