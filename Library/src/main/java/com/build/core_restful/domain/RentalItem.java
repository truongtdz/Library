package com.build.core_restful.domain;

import com.build.core_restful.util.JwtUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "rental_items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant rentalDate;
    private Instant rentedDate;
    private Long lateFee;

    private String bookName;
    private Long rentalPrice;
    private Long depositPrice;
    private Long quantity;
    private Long totalRental;
    private Long totalDeposit;

    private String itemStatus;

    @ManyToOne
    @JoinColumn(name = "rental_order_id")
    private RentalOrder rentalOrder;

    @ManyToOne
    @JoinColumn(name = "rented_order_id")
    private RentedOrder rentedOrder;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

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
