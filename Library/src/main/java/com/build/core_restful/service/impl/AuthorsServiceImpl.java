package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Author;
import com.build.core_restful.domain.request.AuthorsRequest;
import com.build.core_restful.domain.response.AuthorsResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.AuthorsRepository;
import com.build.core_restful.service.AuthorsService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.AuthorsMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorsRepository authorRepository;
    private final AuthorsMapper authorMapper;

    public AuthorsServiceImpl(AuthorsRepository authorRepository, AuthorsMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public PageResponse<Object> getAllAuthors(Pageable pageable) {
        Page<AuthorsResponse> page = authorRepository.findAll(pageable).map(authorMapper::toAuthorResponse);
        return PageResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .content(page.getContent())
                .build();
    }

    @Override
    public AuthorsResponse getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toAuthorResponse)
                .orElseThrow(() -> new NewException("Author with id " + id + " not found!"));
    }

    @Override
    public AuthorsResponse createAuthor(AuthorsRequest authorRequest) {
        if (authorRepository.existsByName(authorRequest.getName())) {
            throw new NewException("Author with name " + authorRequest.getName() + " already exists!");
        }
        Author author = authorMapper.toAuthor(authorRequest);
        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public AuthorsResponse updateAuthor(Long id, AuthorsRequest authorRequest) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NewException("Author with id " + id + " not found!"));
        authorMapper.updateAuthor(author, authorRequest);
        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public boolean deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new NewException("Author with id " + id + " not found!");
        }
        authorRepository.deleteById(id);
        return true;
    }
}
