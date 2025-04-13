package com.build.core_restful.domain.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ItemResponse {
    private long id;
    private Long quantity;
    private Long price;
    private String orderName;
    private String productName;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
