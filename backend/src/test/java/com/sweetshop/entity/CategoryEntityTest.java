package com.sweetshop.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

class CategoryEntityTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void shouldCreateValidCategory() {
        // Given
        Category category = new Category();
        category.setName("Chocolate");
        category.setDescription("Delicious chocolate sweets");
        
        // When
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals("Chocolate", category.getName());
        assertEquals("Delicious chocolate sweets", category.getDescription());
        assertNotNull(category.getCreatedAt());
    }
    
    @Test
    void shouldFailValidationForEmptyName() {
        // Given
        Category category = new Category();
        category.setDescription("Test category");
        
        // When
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }
}
