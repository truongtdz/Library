package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Permission;
import com.build.core_restful.domain.request.PermissionRequest;
import com.build.core_restful.domain.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest permissionRequest);
    PermissionResponse toPermissionResponse(Permission permission);
    void updatePermission(@MappingTarget Permission permission, PermissionRequest permissionRequest);
}
