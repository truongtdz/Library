package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {
    private Long id;

    @NotBlank(message = "Tên quyền không được để trống")
    private String description;

    @NotBlank(message = "API path không được để trống")
    private String apiPath;

    @NotBlank(message = "Phương thức không được để trống")
    private String method;

    @NotBlank(message = "Module không được để trống")
    private String module;
}
