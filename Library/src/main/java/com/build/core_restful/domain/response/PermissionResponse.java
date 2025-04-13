package com.build.core_restful.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PermissionResponse {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
