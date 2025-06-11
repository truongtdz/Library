package com.build.core_restful.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

import com.build.core_restful.domain.listener.BranchListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "branches")
@EntityListeners(BranchListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String city;
    private String district;
    private String ward;
    private String street;
    private Instant openTime;
    private Instant closeTime;  

    private String status;
    private String typeActive;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    List<RentalOrder> rentalOrders;

    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    @JsonIgnore
    List<RentedOrder> rentedOrders;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
