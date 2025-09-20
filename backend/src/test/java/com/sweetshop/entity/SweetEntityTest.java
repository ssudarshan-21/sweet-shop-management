package com.sweetshop.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.Set;

class SweetEntityTest {
    
    private Validator validator;
    private Category testCategory;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Chocolate");
    }
    
    @Test
    void shouldCreateValidSweet() {
        // Given
        Sweet sweet = new Sweet();
        sweet.setName("Dark Chocolate Bar");
        sweet.setDescription("Premium dark chocolate");
        sweet.setPrice(new BigDecimal("5.99"));
        sweet.setQuantity(50);
        sweet.setCategory(testCategory);
        
        // When
        Set<ConstraintViolation<Sweet>> violations = validator.validate(sweet);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals("Dark Chocolate Bar", sweet.getName());
        assertEquals("Premium dark chocolate", sweet.getDescription());
        assertEquals(new BigDecimal("5.99"), sweet.getPrice());
        assertEquals(50, sweet.getQuantity());
        assertEquals(testCategory, sweet.getCategory());
        assertNotNull(sweet.getCreatedAt());
        assertTrue(sweet.isAvailable());
    }
    
    @Test
    void shouldFailValidationForNegativePrice() {
        // Given
        Sweet sweet = new Sweet();
        sweet.setName("Test Sweet");
        sweet.setDescription("Test description");
        sweet.setPrice(new BigDecimal("-1.00"));
        sweet.setQuantity(10);
        sweet.setCategory(testCategory);
        
        // When
        Set<ConstraintViolation<Sweet>> violations = validator.validate(sweet);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    }
    
    @Test
    void shouldFailValidationForNegativeQuantity() {
        // Given
        Sweet sweet = new Sweet();
        sweet.setName("Test Sweet");
        sweet.setDescription("Test description");
        sweet.setPrice(new BigDecimal("5.99"));
        sweet.setQuantity(-5);
        sweet.setCategory(testCategory);
        
        // When
        Set<ConstraintViolation<Sweet>> violations = validator.validate(sweet);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("quantity")));
    }
    
    @Test
    void shouldCalculateAvailabilityBasedOnQuantity() {
        // Given
        Sweet sweet = new Sweet();
        sweet.setName("Test Sweet");
        sweet.setQuantity(0);
        
        // When & Then
        assertFalse(sweet.isAvailable());
        
        sweet.setQuantity(5);
        assertTrue(sweet.isAvailable());
    }
}