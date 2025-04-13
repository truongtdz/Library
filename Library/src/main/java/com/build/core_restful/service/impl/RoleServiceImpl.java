package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Permission;
import com.build.core_restful.domain.Role;
import com.build.core_restful.domain.request.RoleRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.RoleResponse;
import com.build.core_restful.repository.PermissionRepository;
import com.build.core_restful.repository.RoleRepository;
import com.build.core_restful.service.RoleService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.RoleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public PageResponse<Object> getAllRoles(Pageable pageable) {
        Page<RoleResponse> page = roleRepository.findAll(pageable).map(roleMapper::toRoleResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .content(page.getContent())
                .build();
    }

    @Override
    public RoleResponse getRoleById(Long id) {
        if(!roleRepository.existsById(id)){
            throw new NewException("Role have id: " + id + " not exist!");
        }
        return roleRepository.findById(id)
                .map(roleMapper::toRoleResponse)
                .orElse(null);
    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        if(roleRepository.existsByName(roleRequest.getName())){
            throw new NewException("Role have name: " + roleRequest.getName() + " existed!");
        }
        Role role = roleMapper.toRole(roleRequest);
        List<Permission> permissions = new ArrayList<>();
        for(Long id : roleRequest.getPermissionId()){
            if(!permissionRepository.existsById(id)){
                throw new NewException("Permission id: " + id + " not exist!");
            }
            permissions.add(permissionRepository.findById(id).get());
        }
        role.setPermissions(permissions);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        if(roleRepository.existsById(id)){
            throw new NewException("Role have id: " + id + " not exist!");
        }

        Role role = roleRepository.findById(id).get();
        List<Permission> permissions = new ArrayList<>();
        for(Long item : roleRequest.getPermissionId()){
            if(!permissionRepository.existsById(item)){
                throw new NewException("Permission id: " + item + " not exist!");
            }
            permissions.add(permissionRepository.findById(id).get());
        }
        role.setPermissions(permissions);

        roleMapper.updateRole(role, roleRequest);
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public boolean deleteRole(Long id) {
        if(!roleRepository.existsById(id)){
            throw new NewException("Role have id: " + id + " not exist!");
        }
        try{
            roleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
