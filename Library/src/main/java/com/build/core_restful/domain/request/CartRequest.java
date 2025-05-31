package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    private Long quantity;
    private Long rentedDay;
}
