package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookRequest {
    @NotBlank(message = "Tên sách không được để trống")
    private String name;

    private String description;
    private String title;
    private String publisher;
    private Instant publishDate;
    private int pages;
    private String language;
    private Long totalQuantity;
    private Long stock;
    private Long rentalPrice;
    private Long depositPrice;

    @NotNull(message = "Category ID không được để trống")
    private Long categoryId;

    @NotNull(message = "Author ID không được để trống")
    private Long authorsId;

    private List<ImageReq> imageList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageReq{
        private String isDefault;
        private String url;
    }
}
