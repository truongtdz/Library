package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetCoverImageBook {
    @NotBlank(message = "Image id k được để trống")
    private Long imageId;
    @NotBlank(message = "Book id k được để trống")
    private Long bookId;
}
