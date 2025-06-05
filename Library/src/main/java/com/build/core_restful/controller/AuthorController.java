package com.build.core_restful.controller;

import com.build.core_restful.domain.request.AuthorRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.AuthorService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.EntityStatusEnum;

import jakarta.validation.Valid;

import java.util.List;

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

    @GetMapping("/all")
    @AddMessage("Get all authors")
    public ResponseEntity<PageResponse<Object>> getAllAuthors(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "Active") EntityStatusEnum status

    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(authorService.getAllAuthor(
            name, status.toString(), pageable
        ));
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get author by id")
    public ResponseEntity<Object> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping("/create")
    @AddMessage("Create new author")
    public ResponseEntity<Object> createAuthor(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.createAuthor(request));
    }

    @PutMapping("/update/{id}")
    @AddMessage("Update author")
    public ResponseEntity<Object> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(authorService.updateAuthor(id, request));
    }

    @PutMapping("/delete")
    @AddMessage("Delete soft author")
    public ResponseEntity<Boolean> softDelete(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(authorService.softDelete(idList));
    }

    @PutMapping("/restore")
    @AddMessage("Restore author")
    public ResponseEntity<Boolean> restore(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(authorService.restore(idList));
    }

    @DeleteMapping("/delete/{id}")
    @AddMessage("Delete author")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.delete(id));
    }
}
