package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Author;
import com.build.core_restful.domain.request.AuthorRequest;
import com.build.core_restful.domain.request.UploadAvatar;
import com.build.core_restful.domain.response.AuthorResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.AuthorRepository;
import com.build.core_restful.service.AuthorService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.AuthorMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public PageResponse<Object> getAllAuthor(String name, Pageable pageable) {
        Page<Author> page = name.isEmpty() ? 
            authorRepository.findAll(pageable) : authorRepository.findByName(name, pageable);

        Page<AuthorResponse> pageResponse = page.map(authorMapper::toAuthorResponse);
        
        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public AuthorResponse getAuthorById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toAuthorResponse)
                .orElseThrow(() -> new NewException("Author with id " + id + " not found!"));
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest authorRequest) {
        if (authorRepository.existsByName(authorRequest.getName())) {
            throw new NewException("Author with name " + authorRequest.getName() + " already exists!");
        }
        Author author = authorMapper.toAuthor(authorRequest);
        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest) {
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

    @Override
    public AuthorResponse updateAvatar(UploadAvatar uploadAvatar) {
        Author currentAuthor = authorRepository.findById(uploadAvatar.getId()).get();

        currentAuthor.setAvatar(uploadAvatar.getAvtUrl());

        return authorMapper.toAuthorResponse(authorRepository.save(currentAuthor));
    }
}
