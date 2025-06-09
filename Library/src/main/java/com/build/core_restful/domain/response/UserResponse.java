package com.build.core_restful.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String email;
    
    private String fullName;
    private int age;
    private String avatar;
    private String gender;
    private String status;

    private Long totalRental;
    private Long totalRented;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    private RoleRes role;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleRes {
        private Long roleId;
        private String roleName;        
    }
}
