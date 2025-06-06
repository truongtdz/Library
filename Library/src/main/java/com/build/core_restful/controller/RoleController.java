package com.build.core_restful.controller;

import com.build.core_restful.domain.request.RoleRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.RoleService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    @AddMessage("Get all roles")
    public ResponseEntity<PageResponse<Object>> getAllRoles(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);

        PageResponse<Object> roles = roleService.getAllRoles(pageable);

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get role by id")
    public ResponseEntity<Object> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping("/create")
    @AddMessage("Create new role")
    public ResponseEntity<Object> createRole(@Valid @RequestBody RoleRequest newRole) {
        return ResponseEntity.ok(roleService.createRole(newRole));
    }

    @PutMapping("/update/{id}")
    @AddMessage("Update role")
    public ResponseEntity<Object> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest updateRole) {
        return ResponseEntity.ok(roleService.updateRole(id, updateRole));
    }

    @DeleteMapping("/delete/{id}")
    @AddMessage("Delete role")
    public ResponseEntity<Boolean> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
}
