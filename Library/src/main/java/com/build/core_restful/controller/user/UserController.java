package com.build.core_restful.controller.user;

import com.build.core_restful.domain.request.UpdateRoleUserRequest;
import com.build.core_restful.domain.request.UserRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;
import com.build.core_restful.domain.response.UserResponse;
import com.build.core_restful.service.BookService;
import com.build.core_restful.service.UserService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final BookService bookService;

    public UserController(UserService userService, BookService bookService){
        this.userService = userService;
        this.bookService = bookService;
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

    @PostMapping("/upload/{id}")
    @AddMessage("Update avatar user")
    public ResponseEntity<Boolean> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(userService.updateAvatarUser(id, file));
    }

    @PutMapping("/update/role")
    @AddMessage("Update role user")
    public ResponseEntity<UserResponse> updateRole(@Valid @RequestBody UpdateRoleUserRequest updateRoleUserRequest){
        return ResponseEntity.ok(userService.updateRoleUser(updateRoleUserRequest));
    }

    @GetMapping("/search")
    @AddMessage("Get all books")
    public ResponseEntity<SearchResponse> getAllBooks(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(bookService.searchBook(keyword, pageable));
    }

}
