package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private Long id;

    @NotBlank(message = "Tên vai trò không được để trống")
    private String name;
    private String description;

    private List<Long> permissionId;
}
