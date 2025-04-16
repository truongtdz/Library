package com.build.core_restful.domain.response;

import com.build.core_restful.domain.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private UserLoginResponse user;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    static public class UserLoginResponse {
        private Long id;
        private String email;
        private String name;
        private RoleResponse role;
    }
}
