package com.sweetshop.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

class UserEntityTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void shouldCreateValidUser() {
        // Given
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword123");
        user.setFirstName("John");
        user.setLastName("Doe");
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertNotNull(user.getCreatedAt());
        assertTrue(user.isEnabled());
    }
    
    @Test
    void shouldFailValidationForInvalidEmail() {
        // Given
        User user = new User();
        user.setEmail("invalid-email");
        user.setPassword("hashedPassword123");
        user.setFirstName("John");
        user.setLastName("Doe");
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }
    
    @Test
    void shouldFailValidationForEmptyRequiredFields() {
        // Given
        User user = new User();
        
        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }
}
