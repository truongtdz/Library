package com.build.core_restful.controller;

import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;
import com.build.core_restful.service.BookService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.BookStatusEnum;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/available")
    @AddMessage("Get all available books")
    public ResponseEntity<PageResponse<Object>> getAllBooksAvailable(
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooksAvailable(pageable));
    }

    @GetMapping("/unavailable")
    @AddMessage("Get all unavailable books")
    public ResponseEntity<PageResponse<Object>> getAllBooksUnavailable(
            @RequestParam int page,
            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.getAllBooksUnavailable(pageable));
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

    @PutMapping("/delete")
    @AddMessage("Delete book")
    public ResponseEntity<Boolean> softDeleteBook(@RequestParam List<Long> booksId) {
        return ResponseEntity.ok(bookService.softDeleteBooks(booksId));
    }

    @PutMapping("/restore")
    @AddMessage("Restore book")
    public ResponseEntity<Boolean> restoreBook(@RequestParam List<Long> booksId) {
        return ResponseEntity.ok(bookService.restoreBooks(booksId));
    }

    @GetMapping("/search")
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
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return ResponseEntity.ok(bookService.searchBook(
                keyword, categoryId, authorId, language, minPrice, maxPrice, 
                BookStatusEnum.Available.toString(), pageable
        ));
    }

    @GetMapping("/top10/{type}")
    public ResponseEntity<List<BookResponse>> getTop10BookBy(@PathVariable String type){
        return ResponseEntity.ok(bookService.getTop10BookBy(type));
    }

    @GetMapping("/quantity")
    public ResponseEntity<Long> getQuantityBook(){
        return ResponseEntity.ok(bookService.getQuantityBook());
    }
}
