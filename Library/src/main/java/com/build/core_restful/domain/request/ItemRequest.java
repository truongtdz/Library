package com.build.core_restful.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    private Long id;

    @NotNull(message = "Số lượng không được để trống")
    private Long quantity;

    @NotNull(message = "Giá không được để trống")
    private Long price;

    @NotNull(message = "Mã đơn hàng không được để trống")
    private Long orderId;

    @NotNull(message = "Mã sản phẩm không được để trống")
    private Long productId;
}
