package com.sweetshop.repository;

import com.sweetshop.entity.RefreshToken;
import com.sweetshop.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class RefreshTokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("test@example.com", "password123", "Test", "User");
        entityManager.persistAndFlush(testUser);
    }

    @Test
    void shouldFindRefreshTokenByToken() {
        // Given
        RefreshToken refreshToken = new RefreshToken("valid-token-hash", testUser, LocalDateTime.now().plusDays(7));
        entityManager.persistAndFlush(refreshToken);

        // When
        Optional<RefreshToken> found = refreshTokenRepository.findByToken("valid-token-hash");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getToken()).isEqualTo("valid-token-hash");
        assertThat(found.get().getUser()).isEqualTo(testUser);
    }

    @Test
    void shouldReturnEmptyWhenTokenNotFound() {
        // When
        Optional<RefreshToken> found = refreshTokenRepository.findByToken("nonexistent-token");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindRefreshTokenByUser() {
        // Given
        RefreshToken refreshToken = new RefreshToken("user-token-hash", testUser, LocalDateTime.now().plusDays(7));
        entityManager.persistAndFlush(refreshToken);

        // When
        Optional<RefreshToken> found = refreshTokenRepository.findByUser(testUser);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isEqualTo(testUser);
        assertThat(found.get().getToken()).isEqualTo("user-token-hash");
    }

    @Test
    void shouldDeleteExpiredTokens() {
        // Given
        RefreshToken expiredToken = new RefreshToken("expired-token", testUser, LocalDateTime.now().minusDays(1));
        RefreshToken validToken = new RefreshToken("valid-token", testUser, LocalDateTime.now().plusDays(7));
        
        entityManager.persistAndFlush(expiredToken);
        entityManager.persistAndFlush(validToken);

        // When
        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());

        // Then
        assertThat(deletedCount).isEqualTo(1);
        
        // Verify expired token is deleted
        Optional<RefreshToken> expired = refreshTokenRepository.findByToken("expired-token");
        assertThat(expired).isEmpty();
        
        // Verify valid token still exists
        Optional<RefreshToken> valid = refreshTokenRepository.findByToken("valid-token");
        assertThat(valid).isPresent();
    }

    @Test
    void shouldDeleteByUser() {
        // Given
        RefreshToken refreshToken = new RefreshToken("user-token", testUser, LocalDateTime.now().plusDays(7));
        entityManager.persistAndFlush(refreshToken);

        // When
        int deletedCount = refreshTokenRepository.deleteByUser(testUser);

        // Then
        assertThat(deletedCount).isEqualTo(1);
        
        Optional<RefreshToken> found = refreshTokenRepository.findByUser(testUser);
        assertThat(found).isEmpty();
    }
}
