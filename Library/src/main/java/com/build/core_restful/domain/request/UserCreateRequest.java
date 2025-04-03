package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private Long id;

    @NotBlank(message = "Email k được để trống")
    @Size(min = 8, message = "Email phải nhiều hơn 8 kí tự")
    private String email;

    @NotBlank(message = "Password k được để trống")
    @Size(min = 4, message = "Password phải nhiều hơn 4 kí tự")
    private String password;

    private String fullName;
    private String gender;
}
