package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    private Long quantity;
    private Long rentedDay;
}
