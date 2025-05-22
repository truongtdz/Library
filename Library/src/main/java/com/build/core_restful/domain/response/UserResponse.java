package com.build.core_restful.domain.response;

import com.build.core_restful.util.enums.GenderEnum;
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
    private String fullName;
    private String email;
    private GenderEnum gender;
    private int age;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
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
