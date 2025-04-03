package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Permission;
import com.build.core_restful.repository.PermissionRepository;
import com.build.core_restful.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public boolean existById(Long id) {
        return permissionRepository.existsById(id);
    }

    @Override
    public boolean existByName(String name) {
        return permissionRepository.existsByName(name);
    }

    @Override
    public Permission getPermissionById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    @Override
    public Permission getPermissionByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    public Permission createPermission(Permission newPermission) {
        return permissionRepository.save(newPermission);
    }

    @Override
    public Permission updatePermission(Permission updatePermission) {
        return permissionRepository.save(
                Permission.builder()
                        .id(updatePermission.getId())
                        .name(updatePermission.getName())
                        .apiPath(updatePermission.getApiPath())
                        .method(updatePermission.getMethod())
                        .module(updatePermission.getModule())
                        .roles(updatePermission.getRoles())
                        .build()
        );
    }

    @Override
    public void deletePermission(Long id) {
        try {
            permissionRepository.deleteById(id);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
