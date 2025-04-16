package com.build.core_restful.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
