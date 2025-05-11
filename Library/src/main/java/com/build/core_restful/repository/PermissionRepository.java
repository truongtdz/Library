package com.build.core_restful.repository;

import com.build.core_restful.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByDescription(String name);

    List<Permission> findByModule(String module);

    List<Permission> findByModuleAndMethod(String module, String method);

    Permission findByApiPathAndMethod(String api, String method);
}
