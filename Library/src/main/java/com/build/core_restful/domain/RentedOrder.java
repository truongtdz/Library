package com.build.core_restful.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

import com.build.core_restful.domain.listener.RentedOrderListener;
import com.build.core_restful.system.JwtUtil;

@Entity
@Table(name = "rented_orders")
@EntityListeners(RentedOrderListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalLateFee;

    private String city;
    private String district;
    private String ward;
    private String street;
    private String fullName;
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String deliveryMethod;
    private String orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private String shippingMethod;

    private Instant rentedDay;

    @OneToMany(mappedBy = "rentedOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentalItem> items;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

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
