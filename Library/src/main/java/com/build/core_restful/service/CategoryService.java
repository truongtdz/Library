package com.build.core_restful.service;

import com.build.core_restful.domain.request.CategoryRequest;
import com.build.core_restful.domain.response.CategoryResponse;
import com.build.core_restful.domain.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    PageResponse<Object> getAllCategories(String name, Pageable pageable);

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest categoryRequest);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest);

    boolean deleteCategory(Long id);

}
