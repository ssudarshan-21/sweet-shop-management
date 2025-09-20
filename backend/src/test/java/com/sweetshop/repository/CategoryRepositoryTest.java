package com.sweetshop.repository;

import com.sweetshop.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldFindCategoryByName() {
        // Given
        Category category = new Category("Chocolate", "Chocolate sweets");
        entityManager.persistAndFlush(category);

        // When
        Optional<Category> found = categoryRepository.findByName("Chocolate");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Chocolate");
        assertThat(found.get().getDescription()).isEqualTo("Chocolate sweets");
    }

    @Test
    void shouldReturnEmptyWhenCategoryNameNotFound() {
        // When
        Optional<Category> found = categoryRepository.findByName("Nonexistent");

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void shouldCheckIfCategoryExists() {
        // Given
        Category category = new Category("Traditional", "Traditional sweets");
        entityManager.persistAndFlush(category);

        // When & Then
        assertThat(categoryRepository.existsByName("Traditional")).isTrue();
        assertThat(categoryRepository.existsByName("Nonexistent")).isFalse();
    }

    @Test
    void shouldFindCategoriesWithSweets() {
        // Given
        Category chocolateCategory = new Category("Chocolate", "Chocolate sweets");
        Category emptyCategory = new Category("Empty", "No sweets yet");
        
        entityManager.persistAndFlush(chocolateCategory);
        entityManager.persistAndFlush(emptyCategory);

        // When
        var categoriesWithSweets = categoryRepository.findCategoriesWithSweets();

        // Then - Initially should be empty since no sweets are added
        assertThat(categoriesWithSweets).isEmpty();
    }
}