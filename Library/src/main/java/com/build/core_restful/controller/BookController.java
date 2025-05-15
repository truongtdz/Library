package com.build.core_restful.controller;

import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.BookService;
import com.build.core_restful.util.annotation.AddMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @AddMessage("Get all books")
    public ResponseEntity<PageResponse<Object>> getAllBooks(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    @AddMessage("Get book by id")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    @AddMessage("Create book")
    public ResponseEntity<Object> createBook(@Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.createBook(bookRequest));
    }

    @PutMapping("/{id}")
    @AddMessage("Update book")
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
    }

    @DeleteMapping("/{id}")
    @AddMessage("Delete book")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }
}
