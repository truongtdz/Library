package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetCoverImageBook {
    @NotNull(message = "Image id k được để trống")
    private Long imageId;
    @NotNull(message = "Book id k được để trống")
    private Long bookId;
}
