package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetAddressDefault {
    @NotBlank(message = "User id k được để trống")
    private Long userId;
    @NotBlank(message = "Address id k được để trống")
    private Long addressId;
}
