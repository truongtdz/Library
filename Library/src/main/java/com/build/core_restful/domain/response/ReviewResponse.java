package com.build.core_restful.domain.response;

import java.time.Instant;
import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long id;

    private int rate;
    private String image;
    private String comment;
    private Long parentId;
    private List<ReviewResponse> replies;
    private Long bookId;

    private UserRes user;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRes {
        private Long userId;
        private String fullName;
        private String avatar;
    }
}
