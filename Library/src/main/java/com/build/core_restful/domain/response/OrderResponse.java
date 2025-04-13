package com.build.core_restful.domain.response;

import com.build.core_restful.util.enums.OrderStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long totalPrice;
    private OrderStatusEnum status;
    private String userName;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
