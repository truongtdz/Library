package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Author;
import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.request.AuthorRequest;
import com.build.core_restful.domain.response.AuthorResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.AuthorRepository;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.service.AuthorService;
import com.build.core_restful.util.enums.EntityStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.AuthorMapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(
        AuthorRepository authorRepository, 
        AuthorMapper authorMapper,
        BookRepository bookRepository
    ) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public PageResponse<Object> getAllAuthor(String name, String status, Pageable pageable) {
        Page<Author> page = !StringUtils.hasText(name) ? 
            authorRepository.findAll(pageable) : authorRepository.findByNameAndStatus(name, status, pageable);

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
        return authorRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                .map(authorMapper::toAuthorResponse)
                .orElseThrow(() -> new NewException("Author with id " + id + " not found!"));
    }

    @Override
    public AuthorResponse createAuthor(AuthorRequest authorRequest) {
        if (authorRepository.existsByNameAndStatus(authorRequest.getName(), EntityStatusEnum.Active.toString())) {
            throw new NewException("Author with name " + authorRequest.getName() + " already exists!");
        }
        if (authorRepository.existsByNameAndStatus(authorRequest.getName(), EntityStatusEnum.Delete.toString())) {
            throw new NewException("Author with name " + authorRequest.getName() + " already exists at bin!");
        }

        Author author = authorMapper.toAuthor(authorRequest);
        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorRequest authorRequest) {
        Author author = authorRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                .orElseThrow(() -> new NewException("Author with id " + id + " not found!"));
        authorMapper.updateAuthor(author, authorRequest);
        return authorMapper.toAuthorResponse(authorRepository.save(author));
    }

    @Override
    public boolean softDelete(List<Long> idList){
        try {
            for(Long id : idList){
                Author author = authorRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("Author with id: " + id + " not found"));
                    
                author.setStatus(EntityStatusEnum.Delete.toString());

                authorRepository.save(author);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error soft deleting books: " + e.getMessage());
        }
    }
    
    @Override
    public boolean restore(List<Long> idList){
        try {
            for(Long id : idList){
                Author author = authorRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("Author with id: " + id + " not found"));
                    
                author.setStatus(EntityStatusEnum.Active.toString());

                authorRepository.save(author);
            }
            return true;
        } catch (Exception e) {
            throw new NewException("Error soft deleting books: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            List<Book> books = bookRepository.findByCategoryId(id);
            books.forEach(item -> item.setCategory(null));
            bookRepository.saveAll(books);

            authorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new NewException("Delete author with id: " + id + " fail");
        }  
    }
}
