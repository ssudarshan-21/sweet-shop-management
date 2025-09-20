package com.sweetshop.repository;

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
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindRoleByName() {
        // Given
        Role role = new Role("ADMIN", "Administrator role");
        entityManager.persistAndFlush(role);

        // When
        Optional<Role> found = roleRepository.findByName("ADMIN");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("ADMIN");
        assertThat(found.get().getDescription()).isEqualTo("Administrator role");
    }

    @Test
    void shouldReturnEmptyWhenRoleNameNotFound() {
        // When
        Optional<Role> found = roleRepository.findByName("NONEXISTENT");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckIfRoleExists() {
        // Given
        Role role = new Role("USER", "Regular user role");
        entityManager.persistAndFlush(role);

        // When & Then
        assertThat(roleRepository.existsByName("USER")).isTrue();
        assertThat(roleRepository.existsByName("NONEXISTENT")).isFalse();
    }
}
