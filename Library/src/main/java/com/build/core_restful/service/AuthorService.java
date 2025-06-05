package com.build.core_restful.service;

import com.build.core_restful.domain.request.AuthorRequest;
import com.build.core_restful.domain.response.AuthorResponse;
import com.build.core_restful.domain.response.PageResponse;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface AuthorService {
    public PageResponse<Object> getAllAuthor(String name, String status, Pageable pageable);

    AuthorResponse getAuthorById(Long id);

    AuthorResponse createAuthor(AuthorRequest authorRequest);

    AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest);

    boolean softDelete(List<Long> idList);
    
    boolean restore(List<Long> idList);

    boolean delete(Long id);
}
