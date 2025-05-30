package com.build.core_restful.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscribes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    
    private Long age;
    private String gender;
    private String city;
}