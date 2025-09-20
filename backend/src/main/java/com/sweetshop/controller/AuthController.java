package com.sweetshop.controller;

import com.sweetshop.dto.*;
import com.sweetshop.entity.User;
import com.sweetshop.service.AuthService;
import com.sweetshop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDto>> register(
            @Valid @RequestBody UserRegistrationDto registrationDto) {

        User user = userService.registerUser(registrationDto);

        UserResponseDto userResponse = mapToUserResponseDto(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", userResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = authService.login(loginRequest);

        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @RequestBody RefreshTokenRequest request) {

        LoginResponse loginResponse = authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", loginResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", "User logged out"));
    }

    // Utility method to map User entity to UserResponseDto
    private UserResponseDto mapToUserResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles().stream()
                        .map(role -> role.getName()) // assuming Role entity has getName()
                        .collect(Collectors.toSet())
        );
    }

    // Inner class for refresh token request
    public static class RefreshTokenRequest {
        private String refreshToken;

        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }
}
