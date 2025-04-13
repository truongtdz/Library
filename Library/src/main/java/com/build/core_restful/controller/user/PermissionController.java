package com.build.core_restful.controller.user;

import com.build.core_restful.domain.request.PermissionRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.PermissionService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permission")
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @AddMessage("Get all permissions")
    public ResponseEntity<PageResponse<Object>> getAllPermissions(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(permissionService.getAllPermissions(pageable));
    }

    @GetMapping("/{id}")
    @AddMessage("Get permission by id")
    public ResponseEntity<Object> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @PostMapping
    @AddMessage("Create new permission")
    public ResponseEntity<Object> createPermission(@Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.createPermission(request));
    }

    @PutMapping("/{id}")
    @AddMessage("Update permission")
    public ResponseEntity<Object> updatePermission(@PathVariable Long id, @Valid @RequestBody PermissionRequest request) {
        return ResponseEntity.ok(permissionService.updatePermission(id, request));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete permission")
    public ResponseEntity<Boolean> deletePermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.deletePermission(id));
    }
}
