package com.build.core_restful.domain.response;

import com.build.core_restful.domain.Image;
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

    private String categoryName;
    private String authorName;

    private List<ImageResponse> imageList;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ImageResponse{
        private boolean isCover;
        private String url;
    }
}
