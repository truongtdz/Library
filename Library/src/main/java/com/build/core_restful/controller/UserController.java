package com.build.core_restful.controller;

import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UploadAvatarUser;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.service.BookService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService, BookService bookService){
        this.userService = userService;
    }

    @GetMapping
    @AddMessage("Get all user")
    public ResponseEntity<PageResponse<Object>> getAllUsers(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);

        PageResponse<Object> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @AddMessage("Get user by id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    @AddMessage("Create new user")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest newUser) {
        return ResponseEntity.ok(userService.createUser(newUser));
    }

    @PutMapping("/{id}")
    @AddMessage("Update user")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UserRequest updateUser) {
        return ResponseEntity.ok(userService.updateUser(id, updateUser));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Ban user")
    public ResponseEntity<Boolean> banUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.banUser(id));
    }

    @PostMapping("/upload")
    @AddMessage("Update avatar user")
    public ResponseEntity<UserResponse> uploadAvatar(@RequestBody UploadAvatarUser uploadAvatarUser){
        return ResponseEntity.ok(userService.updateAvatarUser(uploadAvatarUser));
    }

    @PutMapping("/update/role")
    @AddMessage("Update role user")
    public ResponseEntity<UserResponse> updateRole(@Valid @RequestBody UpdateRoleUserRequest updateRoleUserRequest){
        return ResponseEntity.ok(userService.updateRoleUser(updateRoleUserRequest));
    }
}
