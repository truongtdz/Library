package com.build.core_restful.domain.request;

import com.build.core_restful.util.enums.OrderStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long id;

    @NotNull(message = "Tổng giá không được để trống")
    private Long totalPrice;

    @NotNull(message = "Trạng thái không được để trống")
    private OrderStatusEnum status;

    private Long userId;
}
