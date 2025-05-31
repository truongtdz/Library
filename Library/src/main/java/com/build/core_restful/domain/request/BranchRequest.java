package com.build.core_restful.domain.request;

import java.time.Instant;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchRequest {
    private String name;
    
    private String city;
    private String district;
    private String ward;
    private String street;
    private Instant openTime;
    private Instant closeTime;  
}
