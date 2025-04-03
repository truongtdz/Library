package com.build.core_restful.service;

import com.build.core_restful.domain.Permission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    List<Permission> getAllPermissions();

    boolean existById(Long id);

    boolean existByName(String name);

    Permission getPermissionById(Long id);

    Permission getPermissionByName(String name);

    Permission createPermission(Permission newPermission);

    Permission updatePermission(Permission updatePermission);

    void deletePermission(Long id);
}
