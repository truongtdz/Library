package com.build.core_restful.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "data_trains")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataTrain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long age;
    private String gender;
    private String city;
    private String categoryId;
}
