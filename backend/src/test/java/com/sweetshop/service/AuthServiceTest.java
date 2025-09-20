package com.sweetshop.service;

import com.sweetshop.entity.User;
import com.sweetshop.entity.RefreshToken;
import com.sweetshop.repository.RefreshTokenRepository;
import com.sweetshop.dto.LoginRequest;
import com.sweetshop.dto.LoginResponse;
import com.sweetshop.exception.InvalidCredentialsException;
import com.sweetshop.exception.InvalidTokenException;
import com.sweetshop.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;
    
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    
    @Mock
    private JwtUtil jwtUtil;
    
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userService, refreshTokenRepository, jwtUtil);
    }

    @Test
    void shouldLoginWithValidCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        User user = new User("test@example.com", "encoded-password", "John", "Doe");
        user.setId(1L);
        
        String expectedAccessToken = "access-token";
        String expectedRefreshToken = "refresh-token";
        
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(userService.validatePassword(user, "password123")).thenReturn(true);
        when(jwtUtil.generateAccessToken(user)).thenReturn(expectedAccessToken);
        when(jwtUtil.generateRefreshToken()).thenReturn(expectedRefreshToken);

        // When
        LoginResponse result = authService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals(expectedAccessToken, result.getAccessToken());
        assertEquals(expectedRefreshToken, result.getRefreshToken());
        assertEquals("Bearer", result.getTokenType());
        
        verify(userService).findByEmail("test@example.com");
        verify(userService).validatePassword(user, "password123");
        verify(jwtUtil).generateAccessToken(user);
        verify(jwtUtil).generateRefreshToken();
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldThrowExceptionForInvalidCredentials() {
        // Given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword");
        User user = new User("test@example.com", "encoded-password", "John", "Doe");
        
        when(userService.findByEmail("test@example.com")).thenReturn(user);
        when(userService.validatePassword(user, "wrongpassword")).thenReturn(false);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.login(loginRequest);
        });
        
        verify(userService).findByEmail("test@example.com");
        verify(userService).validatePassword(user, "wrongpassword");
        verify(jwtUtil, never()).generateAccessToken(any(User.class));
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void shouldRefreshAccessToken() {
        // Given
        String refreshTokenValue = "valid-refresh-token";
        User user = new User("test@example.com", "password", "John", "Doe");
        RefreshToken refreshToken = new RefreshToken(refreshTokenValue, user, LocalDateTime.now().plusDays(7));
        
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";
        
        when(refreshTokenRepository.findByToken(refreshTokenValue)).thenReturn(Optional.of(refreshToken));
        when(jwtUtil.generateAccessToken(user)).thenReturn(newAccessToken);
        when(jwtUtil.generateRefreshToken()).thenReturn(newRefreshToken);

        // When
        LoginResponse result = authService.refreshToken(refreshTokenValue);

        // Then
        assertNotNull(result);
        assertEquals(newAccessToken, result.getAccessToken());
        assertEquals(newRefreshToken, result.getRefreshToken());
        
        verify(refreshTokenRepository).findByToken(refreshTokenValue);
        verify(jwtUtil).generateAccessToken(user);
        verify(jwtUtil).generateRefreshToken();
        verify(refreshTokenRepository).delete(refreshToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldThrowExceptionForExpiredRefreshToken() {
        // Given
        String expiredTokenValue = "expired-refresh-token";
        User user = new User("test@example.com", "password", "John", "Doe");
        RefreshToken expiredToken = new RefreshToken(expiredTokenValue, user, LocalDateTime.now().minusDays(1));
        
        when(refreshTokenRepository.findByToken(expiredTokenValue)).thenReturn(Optional.of(expiredToken));

        // When & Then
        assertThrows(InvalidTokenException.class, () -> {
            authService.refreshToken(expiredTokenValue);
        });
        
        verify(refreshTokenRepository).findByToken(expiredTokenValue);
        verify(refreshTokenRepository).delete(expiredToken);
        verify(jwtUtil, never()).generateAccessToken(any(User.class));
    }

    @Test
    void shouldLogoutUser() {
        // Given
        String refreshTokenValue = "valid-refresh-token";
        User user = new User("test@example.com", "password", "John", "Doe");
        RefreshToken refreshToken = new RefreshToken(refreshTokenValue, user, LocalDateTime.now().plusDays(7));
        
        when(refreshTokenRepository.findByToken(refreshTokenValue)).thenReturn(Optional.of(refreshToken));

        // When
        authService.logout(refreshTokenValue);

        // Then
        verify(refreshTokenRepository).findByToken(refreshTokenValue);
        verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    void shouldCleanupExpiredTokens() {
        // Given
        when(refreshTokenRepository.deleteByExpiryDateBefore(any(LocalDateTime.class))).thenReturn(5);

        // When
        int deletedCount = authService.cleanupExpiredTokens();

        // Then
        assertEquals(5, deletedCount);
        verify(refreshTokenRepository).deleteByExpiryDateBefore(any(LocalDateTime.class));
    }
}
