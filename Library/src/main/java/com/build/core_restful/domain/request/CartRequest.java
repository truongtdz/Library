package com.build.core_restful.domain.request;

import java.util.List;

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

    private List<BookReq> books;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BookReq {
        @NotNull(message = "Book ID cannot be null")
        private Long bookId;
        private Long quantity;
        private Long rentedDay;
    }
    
}
