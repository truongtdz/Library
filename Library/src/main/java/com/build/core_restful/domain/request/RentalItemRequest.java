package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalItemRequest {
    private Long timeRental;
    private Long quantity;

    @NotNull
    private Long bookId;
}
