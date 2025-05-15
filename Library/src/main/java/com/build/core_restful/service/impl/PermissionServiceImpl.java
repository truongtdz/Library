package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Permission;
import com.build.core_restful.domain.request.PermissionRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.PermissionResponse;
import com.build.core_restful.repository.PermissionRepository;
import com.build.core_restful.service.PermissionService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.PermissionMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public PageResponse<Object> getAllPermissions(Pageable pageable) {
        Page<PermissionResponse> page = permissionRepository.findAll(pageable).map(permissionMapper::toPermissionResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .content(page.getContent())
                .build();
    }

    @Override
    public PermissionResponse getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toPermissionResponse)
                .orElseThrow(() -> new NewException("Permission with id " + id + " not found"));
    }

    @Override
    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepository.existsByDescription(request.getDescription())) {
            throw new NewException("Permission with name " + request.getDescription() + " already exists!");
        }
        Permission permission = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public PermissionResponse updatePermission(Long id, PermissionRequest request) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NewException("Permission with id " + id + " not found"));
        permissionMapper.updatePermission(permission, request);
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public boolean deletePermission(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new NewException("Permission with id " + id + " not found!");
        }
        try {
            permissionRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
