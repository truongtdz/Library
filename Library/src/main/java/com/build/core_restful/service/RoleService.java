package com.build.core_restful.service;

import com.build.core_restful.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<Role> getALlRoles();

    boolean existById(Long id);

    boolean existByName(String name);

    Role getRoleById(Long id);

    Role getRoleByName(String name);

    Role createRole(Role newRole);

    Role updateRole(Role updateRole);

    void deleteRole(Long id);
}
