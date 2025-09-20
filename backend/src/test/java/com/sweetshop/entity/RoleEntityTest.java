package com.sweetshop.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

class RoleEntityTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    void shouldCreateValidRole() {
        // Given
        Role role = new Role();
        role.setName("ADMIN");
        role.setDescription("Administrator role");
        
        // When
        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals("ADMIN", role.getName());
        assertEquals("Administrator role", role.getDescription());
    }
    
    @Test
    void shouldFailValidationForEmptyName() {
        // Given
        Role role = new Role();
        role.setDescription("Test role");
        
        // When
        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }
}
