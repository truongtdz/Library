package com.build.core_restful.controller;

import com.build.core_restful.domain.request.AuthorRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.AuthorService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @AddMessage("Get all authors")
    public ResponseEntity<PageResponse<Object>> getAllAuthors(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String name
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(authorService.getAllAuthor(name, pageable));
    }

    @GetMapping("/{id}")
    @AddMessage("Get author by id")
    public ResponseEntity<Object> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    @AddMessage("Create new author")
    public ResponseEntity<Object> createAuthor(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.createAuthor(request));
    }

    @PutMapping("/{id}")
    @AddMessage("Update author")
    public ResponseEntity<Object> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete author")
    public ResponseEntity<Boolean> deleteAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.deleteAuthor(id));
    }
}
