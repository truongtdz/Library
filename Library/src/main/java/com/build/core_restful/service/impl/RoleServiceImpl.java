package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Role;
import com.build.core_restful.repository.RoleRepository;
import com.build.core_restful.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getALlRoles() {
        return roleRepository.findAll();
    }

    @Override
    public boolean existById(Long id) {
        return roleRepository.existsById(id);
    }

    @Override
    public boolean existByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role createRole(Role newRole) {
        return roleRepository.save(newRole);
    }

    @Override
    public Role updateRole(Role updateRole) {
        return roleRepository.save(
                Role.builder()
                        .id(updateRole.getId())
                        .name(updateRole.getName())
                        .permissions(updateRole.getPermissions())
                        .users(updateRole.getUsers())
                        .description(updateRole.getDescription())
                        .build()
        );
    }

    @Override
    public void deleteRole(Long id) {
        try{
            roleRepository.deleteById(id);
        } catch (Exception ex){
            log.error(ex.getMessage());
        }
    }
}
