package com.build.core_restful.domain.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchResponse {
    private Long id;

    private String name;

    private String city;
    private String district;
    private String ward;
    private String street;
    private Instant openTime;
    private Instant closeTime;  

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
}
