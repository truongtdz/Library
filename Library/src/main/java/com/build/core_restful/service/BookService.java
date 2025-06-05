package com.build.core_restful.service;

import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    PageResponse<Object> getAllBooksActive(Pageable pageable);

    PageResponse<Object> getAllBooksDelete(Pageable pageable);

    BookResponse getBookById(Long id);

    BookResponse createBook(BookRequest bookRequest);

    BookResponse updateBook(Long id, BookRequest bookRequest);

    boolean softDeleteBooks(List<Long> booksId);

    boolean restoreBooks(List<Long> booksId);

    void deleteBook(Long id);

    Long getQuantityBookActive();

    Long getQuantityBookDelete();

    SearchResponse searchBook(
            String keyword,
            Long categoryId,
            Long authorId,
            String language,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String bookStatus,
            Pageable pageable
            
    );
}
