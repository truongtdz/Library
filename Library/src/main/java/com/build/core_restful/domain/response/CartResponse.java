package com.build.core_restful.domain.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponse {
    private Long userId;
    private Long quantity;
    private Long rentedDay;
    private BookResponse book;
}
