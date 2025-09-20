package com.sweetshop.controller;

import com.sweetshop.service.AuthService;
import com.sweetshop.service.UserService;
import com.sweetshop.dto.LoginRequest;
import com.sweetshop.dto.LoginResponse;
import com.sweetshop.dto.UserRegistrationDto;
import com.sweetshop.entity.User;
import com.sweetshop.exception.InvalidCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    
	@MockBean
    private AuthService authService;

    
	@MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterNewUser() throws Exception {
        // Given
        UserRegistrationDto registrationDto = new UserRegistrationDto(
            "test@example.com", "password123", "John", "Doe"
        );
        
        User savedUser = new User("test@example.com", "encoded", "John", "Doe");
        savedUser.setId(1L);
        
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(savedUser);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void shouldReturnBadRequestForInvalidRegistration() throws Exception {
        // Given
        UserRegistrationDto invalidDto = new UserRegistrationDto(
            "invalid-email", "", "", ""
        );

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void shouldLoginWithValidCredentials() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        LoginResponse loginResponse = new LoginResponse("access-token", "refresh-token", "Bearer", 1800L);
        
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("refresh-token"));
    }

    @Test
    void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword");
        
        when(authService.login(any(LoginRequest.class)))
            .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void shouldRefreshToken() throws Exception {
        // Given
        String refreshToken = "valid-refresh-token";
        LoginResponse loginResponse = new LoginResponse("new-access-token", "new-refresh-token", "Bearer", 1800L);
        
        when(authService.refreshToken(refreshToken)).thenReturn(loginResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("new-access-token"));
    }
}
