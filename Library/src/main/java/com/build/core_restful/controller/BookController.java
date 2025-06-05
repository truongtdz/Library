package com.build.core_restful.controller;

import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.SearchResponse;
import com.build.core_restful.service.BookService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.EntityStatusEnum;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;



@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/all")
    @AddMessage("search book")
    public ResponseEntity<SearchResponse> searchBook(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "Active") EntityStatusEnum status
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(bookService.searchBook(
                keyword, categoryId, authorId, language, minPrice, maxPrice, 
                status.toString(), pageable
        ));
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get book by id")
    public ResponseEntity<Object> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping("/create")
    @AddMessage("Create book")
    public ResponseEntity<Object> createBook(@Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.createBook(bookRequest));
    }

    @PutMapping("/update/{id}")
    @AddMessage("Update book")
    public ResponseEntity<Object> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.ok(bookService.updateBook(id, bookRequest));
    }

    @PutMapping("/delete")
    @AddMessage("Delete soft book")
    public ResponseEntity<Boolean> softDeleteBook(@RequestParam List<Long> booksId) {
        return ResponseEntity.ok(bookService.softDeleteBooks(booksId));
    }

    @PutMapping("/restore")
    @AddMessage("Restore book")
    public ResponseEntity<Boolean> restoreBook(@RequestParam List<Long> booksId) {
        return ResponseEntity.ok(bookService.restoreBooks(booksId));
    }

    @DeleteMapping("/delete/{id}")
    @AddMessage("Delete book")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    @GetMapping("/quantity/active")
    public ResponseEntity<Long> getQuantityBookActive(){
        return ResponseEntity.ok(bookService.getQuantityBookActive());
    }

    @GetMapping("/quantity/delete")
    public ResponseEntity<Long> getQuantityBookDelete(){
        return ResponseEntity.ok(bookService.getQuantityBookDelete());
    }
}
