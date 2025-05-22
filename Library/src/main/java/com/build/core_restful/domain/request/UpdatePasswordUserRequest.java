package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordUserRequest {
    @NotBlank(message = "User id k được để trống")
    private Long userId;

    @NotBlank(message = "Current password k được để trống")
    private String currentPassword;

    @NotBlank(message = "New password k được để trống")
    private String newPassword;
}
