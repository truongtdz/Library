package com.build.core_restful.controller;

import com.build.core_restful.domain.request.UpdatePasswordUserRequest;
import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.service.BookService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.EntityStatusEnum;
import com.build.core_restful.util.enums.GenderEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name ="User api")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService, BookService bookService){
        this.userService = userService;
    }

    @Operation(summary = "Lấy danh sách người dùng", description = "API này trả về danh sách tất cả người dùng, có hỗ trợ phân trang.")
    @GetMapping("/all")
    @AddMessage("Get all user")
    public ResponseEntity<PageResponse<Object>> getAllUsers(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) GenderEnum gender,
        @RequestParam(required = false) Long roleId,
        @RequestParam(defaultValue = "Active") EntityStatusEnum status
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(userService.getAllUsers(
            keyword, gender, roleId, status.toString(), pageable
        ));
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get user by id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/create")
    @AddMessage("Create new user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest newUser) {
        return ResponseEntity.ok(userService.createUser(newUser));
    }

    @PutMapping("/update/{id}")
    @AddMessage("Update user")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UserRequest updateUser) {
        return ResponseEntity.ok(userService.updateUser(id, updateUser));
    }

    @PutMapping("/delete")
    @AddMessage("Delete soft user")
    public ResponseEntity<Boolean> softDeleteUser(@RequestParam List<Long> usersId) {
        return ResponseEntity.ok(userService.softDeleteUsers(usersId));
    }

    @PutMapping("/restore")
    @AddMessage("Restore user")
    public ResponseEntity<Boolean> restoreUser(@RequestParam List<Long> usersId) {
        return ResponseEntity.ok(userService.restoreUsers(usersId));
    }

    @DeleteMapping("/delete/{id}")
    @AddMessage("Delete user")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/update/password")
    @AddMessage("Update password user")
    public ResponseEntity<Boolean> updatePasswordUser(@RequestBody UpdatePasswordUserRequest userRequest){
        return ResponseEntity.ok(userService.updatePasswordUser(userRequest));
    }

    @PutMapping("/update/role")
    @AddMessage("Update role user")
    public ResponseEntity<UserResponse> updateRole(@Valid @RequestBody UpdateRoleUserRequest updateRoleUserRequest){
        return ResponseEntity.ok(userService.updateRoleUser(updateRoleUserRequest));
    }

    @GetMapping("/quantity/active")
    @AddMessage("Get quantity active")
    public ResponseEntity<Long> getQuantityUserActive(){
        return ResponseEntity.ok(userService.getQuantityUserActive());
    }

    @GetMapping("/quantity/delete")
    @AddMessage("Get quantity delete")
    public ResponseEntity<Long> getQuantityUserDelete(){
        return ResponseEntity.ok(userService.getQuantityUserDelete());
    }
}