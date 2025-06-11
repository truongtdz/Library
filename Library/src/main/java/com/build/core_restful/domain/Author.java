package com.build.core_restful.domain;

import com.build.core_restful.domain.listener.AuthorListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "authors")
@EntityListeners(AuthorListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name không được để trống ")
    private String name;

    private String avatar;
    private String status;
    private String typeActive;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Book> products;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
