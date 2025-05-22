package com.build.core_restful.domain.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponse {
    private Long id;
    private String name;
    private String avatar;
    private String description;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
