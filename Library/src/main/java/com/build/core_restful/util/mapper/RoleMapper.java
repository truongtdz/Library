package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Role;
import com.build.core_restful.domain.request.RoleRequest;
import com.build.core_restful.domain.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleRequest roleRequest);
    RoleResponse toRoleResponse(Role role);
    void updateRole(@MappingTarget Role role, RoleRequest roleRequest);
}
