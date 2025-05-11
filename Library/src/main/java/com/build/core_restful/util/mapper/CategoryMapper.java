package com.build.core_restful.util.mapper;

import com.build.core_restful.domain.Category;
import com.build.core_restful.domain.request.CategoryRequest;
import com.build.core_restful.domain.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest categoryRequest);
    CategoryResponse toCategoryResponse(Category category);
    void updateCategory(@MappingTarget Category category, CategoryRequest categoryRequest);
}
