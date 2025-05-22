package com.build.core_restful.domain;

import com.build.core_restful.util.JwtUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "rental_orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;
    private Long depositPrice;

    private String city;
    private String district;
    private String ward;
    private String street;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    List<RentalItem> items;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.setCreateBy(JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "");

        this.setCreateAt(Instant.now());
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.setUpdateBy(JwtUtil.getCurrentUserLogin().isPresent()
                ? JwtUtil.getCurrentUserLogin().get()
                : "");

        this.setUpdateAt(Instant.now());
    }
}
