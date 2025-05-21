package com.build.core_restful.domain.response;

import com.build.core_restful.util.enums.BookStatusEnum;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Long id;
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
    private BookStatusEnum status;

    private CategoryRes category;
    private AuthorRes author;

    private List<ImageRes> imageList;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageRes{
        private boolean isCover;
        private String url;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AuthorRes{
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CategoryRes{
        private Long id;
        private String name;
    }
}
