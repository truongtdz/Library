package com.build.core_restful.controller.product;

import com.build.core_restful.domain.request.AuthorsRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.AuthorsService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorsController {
    private final AuthorsService authorService;

    public AuthorsController(AuthorsService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @AddMessage("Get all authors")
    public ResponseEntity<PageResponse<Object>> getAllAuthors(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(authorService.getAllAuthors(pageable));
    }

    @GetMapping("/{id}")
    @AddMessage("Get author by id")
    public ResponseEntity<Object> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    @AddMessage("Create new author")
    public ResponseEntity<Object> createAuthor(@Valid @RequestBody AuthorsRequest request) {
        return ResponseEntity.ok(authorService.createAuthor(request));
    }

    @PutMapping("/{id}")
    @AddMessage("Update author")
    public ResponseEntity<Object> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorsRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete author")
    public ResponseEntity<Boolean> deleteAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.deleteAuthor(id));
    }
}
