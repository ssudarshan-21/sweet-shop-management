package com.sweetshop.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

class RefreshTokenEntityTest {
    
    private Validator validator;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
    }
    
    @Test
    void shouldCreateValidRefreshToken() {
        // Given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("valid-refresh-token-hash");
        refreshToken.setUser(testUser);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        
        // When
        Set<ConstraintViolation<RefreshToken>> violations = validator.validate(refreshToken);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals("valid-refresh-token-hash", refreshToken.getToken());
        assertEquals(testUser, refreshToken.getUser());
        assertNotNull(refreshToken.getExpiryDate());
        assertNotNull(refreshToken.getCreatedAt());
    }
    
    @Test
    void shouldFailValidationForEmptyToken() {
        // Given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(testUser);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        
        // When
        Set<ConstraintViolation<RefreshToken>> violations = validator.validate(refreshToken);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("token")));
    }
    
    @Test
    void shouldCheckIfTokenIsExpired() {
        // Given
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(LocalDateTime.now().minusDays(1));
        
        // When & Then
        assertTrue(refreshToken.isExpired());
        
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        assertFalse(refreshToken.isExpired());
    }
}