package com.build.core_restful.service;

import com.build.core_restful.domain.request.AuthorRequest;
import com.build.core_restful.domain.request.UploadAvatar;
import com.build.core_restful.domain.response.AuthorResponse;
import com.build.core_restful.domain.response.PageResponse;

import org.springframework.data.domain.Pageable;

public interface AuthorService {
    PageResponse<Object> getAllAuthor(String name, Pageable pageable);

    AuthorResponse getAuthorById(Long id);

    AuthorResponse createAuthor(AuthorRequest authorRequest);

    AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest);

    boolean deleteAuthor(Long id);

    AuthorResponse updateAvatar(UploadAvatar uploadAvatar);
}
