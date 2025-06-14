package com.build.core_restful.domain;

import com.build.core_restful.domain.listener.BookListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "books")
@EntityListeners(BookListener.class)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name không được để trống ")
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long lateFee;

    private String publisher;
    private Instant publishDate;
    private Long pages;
    private String language;
    private Long totalQuantity;
    private Long stock;
    private Long rentalPrice;
    private Long depositPrice;

    private String typeActive;
    private String status;

    @Column(nullable = false)
    @Builder.Default
    private Long quantityViewed = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long quantityRented = 0L;

    @Column(nullable = false)
    @Builder.Default
    private Long quantityLiked = 0L;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RentalItem> items;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Image> images;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Cart> carts;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
