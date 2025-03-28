package com.build.core_restful.controller;

import com.build.core_restful.domain.request.UserCreateRequest;
import com.build.core_restful.domain.request.UserUpdateRequest;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.exception.NewException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id){
        if(!userService.existUserById(id)){
            throw new NewException("Người dùng id = " + id + " không tồn tại ");
        }

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserCreateRequest newUser) {
        if(userService.existUserByEmail(newUser.getEmail())){
            throw new NewException("Người dùng id = " + newUser.getId() + " đã tồn tại ");
        }

        return ResponseEntity.ok(userService.createUser(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest updateUser) {
        if(!userService.existUserById(id)){
            throw new NewException("Người dùng id = " + id + " không tồn tại ");
        }

        return ResponseEntity.ok(userService.updateUser(updateUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if(!userService.existUserById(id)){
            throw new NewException("Người dùng id = " + id + " không tồn tại ");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }
}
