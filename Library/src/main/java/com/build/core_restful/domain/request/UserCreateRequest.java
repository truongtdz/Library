package com.build.core_restful.domain.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String gender;
}
