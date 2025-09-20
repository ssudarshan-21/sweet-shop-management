package com.sweetshop.service;

import com.sweetshop.entity.User;
import com.sweetshop.entity.RefreshToken;
import com.sweetshop.repository.RefreshTokenRepository;
import com.sweetshop.dto.LoginRequest;
import com.sweetshop.dto.LoginResponse;
import com.sweetshop.exception.InvalidCredentialsException;
import com.sweetshop.exception.InvalidTokenException;
import com.sweetshop.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthService {

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Login user and return access + refresh tokens
     */
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());

        if (!userService.validatePassword(user, loginRequest.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("Account is disabled");
        }

        // Generate access + refresh tokens
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshTokenValue = jwtUtil.generateRefreshToken();

        // Delete all existing refresh tokens for this user and flush
        refreshTokenRepository.deleteAllByUser(user);
        refreshTokenRepository.flush(); // ensures DB executes delete immediately

        // Save new refresh token
        RefreshToken refreshToken = new RefreshToken(
            refreshTokenValue,
            user,
            LocalDateTime.now().plusDays(7)
        );
        refreshTokenRepository.save(refreshToken);

        return new LoginResponse(accessToken, refreshTokenValue, "Bearer", 1800L); // 30 minutes expiry
    }

    /**
     * Refresh access token using a valid refresh token
     */
    public LoginResponse refreshToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("Refresh token has expired");
        }

        User user = refreshToken.getUser();

        // Generate new tokens
        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshTokenValue = jwtUtil.generateRefreshToken();

        // Delete old token and flush
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();

        // Save new refresh token
        RefreshToken newRefreshToken = new RefreshToken(
            newRefreshTokenValue,
            user,
            LocalDateTime.now().plusDays(7)
        );
        refreshTokenRepository.save(newRefreshToken);

        return new LoginResponse(newAccessToken, newRefreshTokenValue, "Bearer", 1800L);
    }

    /**
     * Logout user by deleting the provided refresh token
     */
    public void logout(String refreshTokenValue) {
        refreshTokenRepository.findByToken(refreshTokenValue)
                .ifPresent(token -> {
                    refreshTokenRepository.delete(token);
                    refreshTokenRepository.flush();
                });
    }

    /**
     * Cleanup all expired refresh tokens
     */
    public int cleanupExpiredTokens() {
        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
        refreshTokenRepository.flush();
        return deletedCount;
    }
}
