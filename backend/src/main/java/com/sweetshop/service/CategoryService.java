package com.sweetshop.service;

import com.sweetshop.entity.Category;
import com.sweetshop.repository.CategoryRepository;
import com.sweetshop.dto.CategoryDto;
import com.sweetshop.exception.CategoryNotFoundException;
import com.sweetshop.exception.CategoryAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new CategoryAlreadyExistsException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findAllCategories() {
        return categoryRepository.findAllOrderByName();
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Category findByName(String name) {
        return categoryRepository.findByName(name)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with name: " + name));
    }

    public Category updateCategory(Long id, CategoryDto categoryDto) {
        Category category = findById(id);

        // Check if new name conflicts with existing categories (excluding current)
        if (!category.getName().equals(categoryDto.getName()) && 
            categoryRepository.existsByName(categoryDto.getName())) {
            throw new CategoryAlreadyExistsException("Category with name '" + categoryDto.getName() + "' already exists");
        }

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = findById(id);
        
        // Check if category has associated sweets
        if (!category.getSweets().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated sweets");
        }

        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<Category> findCategoriesWithSweets() {
        return categoryRepository.findCategoriesWithSweets();
    }

    @Transactional(readOnly = true)
    public boolean isCategoryNameAvailable(String name) {
        return !categoryRepository.existsByName(name);
    }
}