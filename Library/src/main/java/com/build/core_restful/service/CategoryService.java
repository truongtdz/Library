package com.build.core_restful.service;

import com.build.core_restful.domain.request.CategoryRequest;
import com.build.core_restful.domain.response.CategoryResponse;
import com.build.core_restful.domain.response.PageResponse;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface CategoryService {
    PageResponse<Object> getAllCategories(String name, String status, Pageable pageable);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    boolean softDelete(List<Long> idList);
    
    boolean restore(List<Long> idList);

    boolean delete(Long id);
}
