package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalItemRequest {
    @NotNull
    private Long bookId;

    private Long rentedDay;
    private Long quantity;
}
