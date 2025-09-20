package com.sweetshop.controller;

import com.sweetshop.service.CategoryService;
import com.sweetshop.entity.Category;
import com.sweetshop.dto.CategoryDto;
import com.sweetshop.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully", category));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Category created successfully", category));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id, 
                                                               @Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", "Category deleted"));
    }

    @GetMapping("/with-sweets")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoriesWithSweets() {
        List<Category> categories = categoryService.findCategoriesWithSweets();
        return ResponseEntity.ok(ApiResponse.success("Categories with sweets retrieved successfully", categories));
    }
}