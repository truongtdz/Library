package com.build.core_restful.domain.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeRequest {
    private String fullName;
    private String email;
    private Long age;
    private String gender;
    private String city;
}
