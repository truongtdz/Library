package com.build.core_restful.controller;

import com.build.core_restful.domain.Permission;
import com.build.core_restful.service.PermissionService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.exception.NewException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permission")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @AddMessage("Get all permissions")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    @AddMessage("Get permission by id")
    public ResponseEntity<Object> getPermissionById(@PathVariable Long id) {
        if (!permissionService.existById(id)) {
            throw new NewException("Permission id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PostMapping
    @AddMessage("Create permission")
    public ResponseEntity<Object> createPermission(@RequestBody Permission newPermission) {
        if (permissionService.existByName(newPermission.getName())) {
            throw new NewException("Name = " + newPermission.getName() + " đã tồn tại");
        }
        return ResponseEntity.ok(permissionService.createPermission(newPermission));
    }

    @PutMapping("/{id}")
    @AddMessage("Update permission")
    public ResponseEntity<Object> updatePermission(@PathVariable Long id, @RequestBody Permission updatePermission) {
        if (!permissionService.existById(id)) {
            throw new NewException("Permission id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok(permissionService.updatePermission(updatePermission));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete permission")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        if (!permissionService.existById(id)) {
            throw new NewException("Permission id = " + id + " không tồn tại");
        }
        permissionService.deletePermission(id);
        return ResponseEntity.ok(null);
    }
}
