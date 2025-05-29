package com.build.core_restful.domain.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private int rate;
    private String image;
    private String comment;
    private Long parentId;
    private Long bookId;
    private Long userId;
}
