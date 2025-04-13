package com.build.core_restful.service;

import com.build.core_restful.domain.request.PermissionRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.PermissionResponse;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    PageResponse<Object> getAllPermissions(Pageable pageable);

    PermissionResponse getPermissionById(Long id);

    PermissionResponse createPermission(PermissionRequest request);

    PermissionResponse updatePermission(Long id, PermissionRequest request);

    boolean deletePermission(Long id);

}
