package com.build.core_restful.controller;

import com.build.core_restful.domain.request.CategoryRequest;
import com.build.core_restful.domain.response.CategoryResponse;
import com.build.core_restful.domain.response.PageResponse;
import com.build.core_restful.service.CategoryService;
import com.build.core_restful.util.annotation.AddMessage;
import com.build.core_restful.util.enums.EntityStatusEnum;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    @AddMessage("Get all categories")
    public ResponseEntity<PageResponse<Object>> getAllCategories(
        @RequestParam int page, 
        @RequestParam int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String sortDir,
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "Active") EntityStatusEnum status
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy != null ? sortBy : "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(categoryService.getAllCategories(
            name, status.toString(), pageable
        ));
    }

    @GetMapping("/by/{id}")
    @AddMessage("Get category by id")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("/create")
    @AddMessage("Create new category")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.createCategory(categoryRequest));
    }

    @PutMapping("/update/{id}")
    @AddMessage("Update category")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id,
                                                           @Valid @RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequest));
    }

    @PutMapping("/delete")
    @AddMessage("Delete soft author")
    public ResponseEntity<Boolean> softDelete(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(categoryService.softDelete(idList));
    }

    @PutMapping("/restore")
    @AddMessage("Restore author")
    public ResponseEntity<Boolean> restore(@RequestParam List<Long> idList) {
        return ResponseEntity.ok(categoryService.restore(idList));
    }

    @DeleteMapping("/delete/{id}")
    @AddMessage("Delete author")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }
}
