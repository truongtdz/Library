package com.build.core_restful.service;

import com.build.core_restful.domain.request.RoleRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RoleResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    PageResponse<Object> getAllRoles(Pageable pageable);

    RoleResponse getRoleById(Long id);

    RoleResponse createRole(RoleRequest role);

    RoleResponse updateRole(Long id, RoleRequest role);

    boolean deleteRole(Long id);

}
