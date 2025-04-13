package com.build.core_restful.domain.response;

import com.build.core_restful.domain.Permission;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class RoleResponse {
    private Long id;
    private String name;
    private String description;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
    private List<Permission> permissions;
}
