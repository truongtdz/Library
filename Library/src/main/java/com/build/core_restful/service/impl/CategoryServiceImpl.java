package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Book;
import com.build.core_restful.domain.Category;
import com.build.core_restful.domain.request.CategoryRequest;
import com.build.core_restful.domain.response.CategoryResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.BookRepository;
import com.build.core_restful.repository.CategoryRepository;
import com.build.core_restful.service.CategoryService;
import com.build.core_restful.util.enums.EntityStatusEnum;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.CategoryMapper;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(
        CategoryRepository categoryRepository, 
        CategoryMapper categoryMapper,
        BookRepository bookRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.bookRepository = bookRepository;
    };

    @Override
    public PageResponse<Object> getAllCategories(String name, String status, Pageable pageable) {
        Page<Category> page = StringUtils.hasText(name) ? 
            categoryRepository.findByNameAndStatus(name, status, pageable) : categoryRepository.findAll(pageable);

        Page<CategoryResponse> pageResponse = page.map(categoryMapper::toCategoryResponse);
        
        return PageResponse.builder()
                .page(pageResponse.getNumber())
                .size(pageResponse.getSize())
                .totalPages(pageResponse.getTotalPages())
                .totalElements(pageResponse.getTotalElements())
                .content(pageResponse.getContent())
                .build();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(() -> new NewException("Category with id " + id + " not found"));
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByNameAndStatus(categoryRequest.getName(), EntityStatusEnum.Active.toString())) {
            throw new NewException("Category with name " + categoryRequest.getName() + " already exists!");
        }
        if (categoryRepository.existsByNameAndStatus(categoryRequest.getName(), EntityStatusEnum.Delete.toString())) {
            throw new NewException("Category with name " + categoryRequest.getName() + " already exists at bin!");
        }
        Category category = categoryMapper.toCategory(categoryRequest);
        category.setStatus(EntityStatusEnum.Active.toString());
        category.setTypeActive("CREATE");
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                .orElseThrow(() -> new NewException("Category with id " + id + " not found"));
        categoryMapper.updateCategory(category, categoryRequest);
        category.setTypeActive("UPDATE");
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public boolean softDelete(List<Long> idList){
        try {
            for(Long id : idList){
                Category category = categoryRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("Category with id: " + id + " not found"));
                    
                category.setStatus(EntityStatusEnum.Delete.toString());
                category.setTypeActive("DELETE");
                categoryRepository.save(category);
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
                Category category = categoryRepository.findByIdAndStatus(id, EntityStatusEnum.Active.toString())
                    .orElseThrow(() -> new NewException("Category with id: " + id + " not found"));
                    
                category.setStatus(EntityStatusEnum.Active.toString());
                category.setTypeActive("RESTORE");
                categoryRepository.save(category);
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
            books.forEach(
                item -> {
                    item.setCategory(null);
                    item.setTypeActive(null);
                }
            );
            bookRepository.saveAll(books);

            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new NewException("Delete author with id: " + id + " fail");
        }  
    }
}
