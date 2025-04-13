package com.build.core_restful.domain;

import com.build.core_restful.util.enums.GenderEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trains")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String category;

    private String authors;
}
