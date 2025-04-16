package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutRequest {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private List<ItemRequest> items;
}
