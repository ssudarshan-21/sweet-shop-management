package com.sweetshop.repository;

import com.sweetshop.entity.Sweet;
import com.sweetshop.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class SweetRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SweetRepository sweetRepository;

    private Category chocolateCategory;
    private Category traditionalCategory;

    @BeforeEach
    void setUp() {
        chocolateCategory = new Category("Chocolate", "Chocolate sweets");
        traditionalCategory = new Category("Traditional", "Traditional sweets");
        
        entityManager.persistAndFlush(chocolateCategory);
        entityManager.persistAndFlush(traditionalCategory);
    }

    @Test
    void shouldFindSweetsByCategory() {
        // Given
        Sweet chocolateBar = new Sweet("Chocolate Bar", "Dark chocolate", new BigDecimal("5.99"), 10, chocolateCategory);
        Sweet traditionalSweet = new Sweet("Gulab Jamun", "Sweet balls", new BigDecimal("3.99"), 20, traditionalCategory);
        
        entityManager.persistAndFlush(chocolateBar);
        entityManager.persistAndFlush(traditionalSweet);

        // When
        List<Sweet> chocolateSweets = sweetRepository.findByCategory(chocolateCategory);
        List<Sweet> traditionalSweets = sweetRepository.findByCategory(traditionalCategory);

        // Then
        assertThat(chocolateSweets).hasSize(1);
        assertThat(chocolateSweets.get(0).getName()).isEqualTo("Chocolate Bar");
        
        assertThat(traditionalSweets).hasSize(1);
        assertThat(traditionalSweets.get(0).getName()).isEqualTo("Gulab Jamun");
    }

    @Test
    void shouldFindAvailableSweets() {
        // Given
        Sweet availableSweet = new Sweet("Available Sweet", "In stock", new BigDecimal("4.99"), 5, chocolateCategory);
        Sweet unavailableSweet = new Sweet("Out of Stock", "Not available", new BigDecimal("6.99"), 0, chocolateCategory);
        
        entityManager.persistAndFlush(availableSweet);
        entityManager.persistAndFlush(unavailableSweet);

        // When
        List<Sweet> availableSweets = sweetRepository.findByQuantityGreaterThan(0);

        // Then
        assertThat(availableSweets).hasSize(1);
        assertThat(availableSweets.get(0).getName()).isEqualTo("Available Sweet");
    }

    @Test
    void shouldFindSweetsByPriceRange() {
        // Given
        Sweet cheapSweet = new Sweet("Cheap Sweet", "Low price", new BigDecimal("2.99"), 10, chocolateCategory);
        Sweet expensiveSweet = new Sweet("Expensive Sweet", "High price", new BigDecimal("12.99"), 5, chocolateCategory);
        Sweet mediumSweet = new Sweet("Medium Sweet", "Medium price", new BigDecimal("7.99"), 8, chocolateCategory);
        
        entityManager.persistAndFlush(cheapSweet);
        entityManager.persistAndFlush(expensiveSweet);
        entityManager.persistAndFlush(mediumSweet);

        // When
        List<Sweet> sweetsInRange = sweetRepository.findByPriceBetween(
            new BigDecimal("5.00"), new BigDecimal("10.00")
        );

        // Then
        assertThat(sweetsInRange).hasSize(1);
        assertThat(sweetsInRange.get(0).getName()).isEqualTo("Medium Sweet");
    }

    @Test
    void shouldSearchSweetsByName() {
        // Given
        Sweet chocolateBar = new Sweet("Dark Chocolate Bar", "Premium dark chocolate", new BigDecimal("8.99"), 10, chocolateCategory);
        Sweet chocolateCake = new Sweet("Chocolate Cake", "Delicious cake", new BigDecimal("15.99"), 3, chocolateCategory);
        Sweet vanillaCake = new Sweet("Vanilla Cake", "Sweet vanilla", new BigDecimal("14.99"), 5, traditionalCategory);
        
        entityManager.persistAndFlush(chocolateBar);
        entityManager.persistAndFlush(chocolateCake);
        entityManager.persistAndFlush(vanillaCake);

        // When
        List<Sweet> chocolateSweets = sweetRepository.findByNameContainingIgnoreCase("chocolate");
        List<Sweet> cakeSweets = sweetRepository.findByNameContainingIgnoreCase("cake");

        // Then
        assertThat(chocolateSweets).hasSize(2);
        assertThat(cakeSweets).hasSize(2);
    }

    @Test
    void shouldFindTopSellingSweets() {
        // Given
        Sweet popularSweet = new Sweet("Popular Sweet", "Best seller", new BigDecimal("6.99"), 100, chocolateCategory);
        Sweet regularSweet = new Sweet("Regular Sweet", "Normal seller", new BigDecimal("4.99"), 50, chocolateCategory);
        Sweet unpopularSweet = new Sweet("Unpopular Sweet", "Slow seller", new BigDecimal("3.99"), 10, chocolateCategory);
        
        entityManager.persistAndFlush(popularSweet);
        entityManager.persistAndFlush(regularSweet);
        entityManager.persistAndFlush(unpopularSweet);

        // When
        List<Sweet> topSweets = sweetRepository.findTopSellingSweets(PageRequest.of(0, 2));

        // Then
        assertThat(topSweets).hasSize(2);
        assertThat(topSweets.get(0).getName()).isEqualTo("Popular Sweet");
        assertThat(topSweets.get(1).getName()).isEqualTo("Regular Sweet");
    }

    @Test
    void shouldFindLowStockSweets() {
        // Given
        Sweet lowStockSweet = new Sweet("Low Stock", "Almost out", new BigDecimal("5.99"), 2, chocolateCategory);
        Sweet goodStockSweet = new Sweet("Good Stock", "Plenty available", new BigDecimal("4.99"), 50, chocolateCategory);
        
        entityManager.persistAndFlush(lowStockSweet);
        entityManager.persistAndFlush(goodStockSweet);

        // When
        List<Sweet> lowStockSweets = sweetRepository.findLowStockSweets(5);

        // Then
        assertThat(lowStockSweets).hasSize(1);
        assertThat(lowStockSweets.get(0).getName()).isEqualTo("Low Stock");
    }
}