package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequest {
    @NotBlank(message = "Name không được để trống")
    private String name;

    private String avatar;
    private String description;
}
