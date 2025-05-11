package com.build.core_restful.service;

import com.build.core_restful.domain.request.AuthorsRequest;
import com.build.core_restful.domain.response.AuthorsResponse;
import com.build.core_restful.domain.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AuthorsService {
    PageResponse<Object> getAllAuthors(Pageable pageable);

    AuthorsResponse getAuthorById(Long id);

    AuthorsResponse createAuthor(AuthorsRequest authorRequest);

    AuthorsResponse updateAuthor(Long id, AuthorsRequest authorRequest);

    boolean deleteAuthor(Long id);

}
