package com.build.core_restful.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String name;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
