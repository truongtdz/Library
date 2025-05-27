package com.build.core_restful.service.impl;

import com.build.core_restful.domain.Category;
import com.build.core_restful.domain.request.CategoryRequest;
import com.build.core_restful.domain.response.CategoryResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.repository.CategoryRepository;
import com.build.core_restful.service.CategoryService;
import com.build.core_restful.util.exception.NewException;
import com.build.core_restful.util.mapper.CategoryMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public PageResponse<Object> getAllCategories(String name, Pageable pageable) {
        Page<Category> page = StringUtils.hasText(name) ? 
            categoryRepository.findByName(name, pageable) : categoryRepository.findAll(pageable);

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
        return categoryRepository.findById(id)
                .map(categoryMapper::toCategoryResponse)
                .orElseThrow(() -> new NewException("Category with id " + id + " not found"));
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new NewException("Category with name: " + categoryRequest.getName() + " already exists!");
        }
        Category category = categoryMapper.toCategory(categoryRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NewException("Category with id " + id + " not found"));
        categoryMapper.updateCategory(category, categoryRequest);
        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public boolean deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NewException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
        return true;
    }
}
