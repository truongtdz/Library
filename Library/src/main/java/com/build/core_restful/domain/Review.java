package com.build.core_restful.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.build.core_restful.system.JwtUtil;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rate;
    private String image;
    private Long parentId;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

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
