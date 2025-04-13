package com.build.core_restful.domain.request;

import com.build.core_restful.util.enums.BookStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

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
    private Instant publish_date;
    private int pages;
    private String language;
    private Long totalQuantity;
    private Long stock;
    private Long rentalPrice;
    private Long depositPrice;
    private Long discount;

    @NotNull(message = "Trạng thái sách không được để trống")
    private BookStatusEnum status;

    @NotNull(message = "Category ID không được để trống")
    private Long categoryId;

    @NotNull(message = "Author ID không được để trống")
    private Long authorsId;
}
