package com.build.core_restful.domain;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "data_saves")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startAge;
    private Long endAge;
    private String gender;
    private String city;
    private Long categoryId;

    private Double confidence;         
    private Integer sampleSize;         
    private Integer categoryCount;     
    private Instant createdDate;
    private String algorithm;  
}
