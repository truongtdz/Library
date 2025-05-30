package com.build.core_restful.domain.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookSendEmailResponse {
    private Long id;
    private String name;
    private Long stock;
    private Long rentalPrice;
    private String categoryName;
    private String authorName;
    private String imageUrl;
}
