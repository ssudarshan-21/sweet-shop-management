package com.sweetshop.repository;

import com.sweetshop.entity.User;
import com.sweetshop.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        // Given
        User user = new User("test@example.com", "password123", "John", "Doe");
        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByEmail("test@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void shouldReturnEmptyWhenEmailNotFound() {
        // When
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckIfEmailExists() {
        // Given
        User user = new User("test@example.com", "password123", "John", "Doe");
        entityManager.persistAndFlush(user);

        // When & Then
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    void shouldFindUsersByRole() {
        // Given
        Role adminRole = new Role("ADMIN", "Administrator");
        Role userRole = new Role("USER", "Regular User");
        entityManager.persistAndFlush(adminRole);
        entityManager.persistAndFlush(userRole);

        User adminUser = new User("admin@example.com", "password123", "Admin", "User");
        adminUser.addRole(adminRole);
        
        User regularUser = new User("user@example.com", "password123", "Regular", "User");
        regularUser.addRole(userRole);
        
        entityManager.persistAndFlush(adminUser);
        entityManager.persistAndFlush(regularUser);

        // When
        var adminUsers = userRepository.findByRolesName("ADMIN");
        var regularUsers = userRepository.findByRolesName("USER");

        // Then
        assertThat(adminUsers).hasSize(1);
        assertThat(adminUsers.get(0).getEmail()).isEqualTo("admin@example.com");
        
        assertThat(regularUsers).hasSize(1);
        assertThat(regularUsers.get(0).getEmail()).isEqualTo("user@example.com");
    }

    @Test
    void shouldFindEnabledUsers() {
        // Given
        User enabledUser = new User("enabled@example.com", "password123", "Enabled", "User");
        enabledUser.setEnabled(true);
        
        User disabledUser = new User("disabled@example.com", "password123", "Disabled", "User");
        disabledUser.setEnabled(false);
        
        entityManager.persistAndFlush(enabledUser);
        entityManager.persistAndFlush(disabledUser);

        // When
        var enabledUsers = userRepository.findByEnabled(true);
        var disabledUsers = userRepository.findByEnabled(false);

        // Then
        assertThat(enabledUsers).hasSize(1);
        assertThat(enabledUsers.get(0).getEmail()).isEqualTo("enabled@example.com");
        
        assertThat(disabledUsers).hasSize(1);
        assertThat(disabledUsers.get(0).getEmail()).isEqualTo("disabled@example.com");
    }
}