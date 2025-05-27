package com.build.core_restful.service;

import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;
import com.build.core_restful.util.enums.TypeQuantityBook;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    PageResponse<Object> getAllBooks(Pageable pageable);

    BookResponse getBookById(Long id);

    BookResponse createBook(BookRequest bookRequest);

    BookResponse updateBook(Long id, BookRequest bookRequest);

    boolean deleteBook(Long id);

    List<BookResponse> getTop10BookBy(String getBookBy);

    Integer getQuantityBook(TypeQuantityBook quantityBook);

    SearchResponse searchBook(
            String keyword,
            Long categoryId,
            Long authorId,
            String language,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}
