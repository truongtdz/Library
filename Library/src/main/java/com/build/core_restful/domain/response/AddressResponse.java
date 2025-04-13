package com.build.core_restful.domain.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private Long id;

    private String city;
    private String district;
    private String ward;
    private String street;
    private String province;
    private boolean isDefault;

    private Long userId;
    private String userName; // hoặc email tuỳ bạn chọn trả về cái nào

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
