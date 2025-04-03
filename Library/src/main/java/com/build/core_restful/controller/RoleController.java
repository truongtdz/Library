package com.build.core_restful.controller;

import com.build.core_restful.domain.Role;
import com.build.core_restful.service.RoleService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.exception.NewException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping
    @AddMessage("Get all role")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getALlRoles();

        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @AddMessage("Get role by id")
    public ResponseEntity<Object> getUserById(@PathVariable Long id){
        if(!roleService.existById(id)){
            throw new NewException("Role id = " + id + " không tồn tại ");
        }

        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @PostMapping
    @AddMessage("Create role")
    public ResponseEntity<Object> createUser(@RequestBody Role newRole) {
        if(roleService.existByName(newRole.getName())){
            throw new NewException("Name = " + newRole.getName() + " đã tồn tại ");
        }

        return ResponseEntity.ok(roleService.createRole(newRole));
    }

    @PutMapping("/{id}")
    @AddMessage("Update role")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody Role updateRole) {
        if(!roleService.existById(id)){
            throw new NewException("Role id = " + id + " không tồn tại ");
        }

        return ResponseEntity.ok(roleService.updateRole(updateRole));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete role")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if(!roleService.existById(id)){
            throw new NewException("Role id = " + id + " không tồn tại ");
        }
        roleService.deleteRole(id);
        return ResponseEntity.ok(null);
    }
}
