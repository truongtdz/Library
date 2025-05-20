package com.build.core_restful.service;

import com.build.core_restful.domain.request.BookRequest;
import com.build.core_restful.domain.response.BookResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.domain.response.SearchResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    PageResponse<Object> getAllBooks(Pageable pageable);

    BookResponse getBookById(Long id);

    BookResponse createBook(BookRequest bookRequest);

    BookResponse updateBook(Long id, BookRequest bookRequest);

    boolean deleteBook(Long id);

    SearchResponse searchBook(String keyword, Pageable pageable);

    PageResponse<Object> getBooksByCategory(Long id, Pageable pageable);

    PageResponse<Object> getBooksByAuthor(Long id, Pageable pageable);
}
