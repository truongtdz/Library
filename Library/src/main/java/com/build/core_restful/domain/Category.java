package com.build.core_restful.domain;


import com.build.core_restful.domain.listener.CategoryListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "categories")
@EntityListeners(CategoryListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name không được để trống ")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;
    private String typeActive;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Book> products;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
