package com.sweetshop.service;

import com.sweetshop.entity.Sweet;
import com.sweetshop.entity.Category;
import com.sweetshop.repository.SweetRepository;
import com.sweetshop.repository.CategoryRepository;
import com.sweetshop.dto.SweetDto;
import com.sweetshop.dto.SweetSearchCriteria;
import com.sweetshop.exception.SweetNotFoundException;
import com.sweetshop.exception.InsufficientStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SweetServiceTest {

    @Mock
    private SweetRepository sweetRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    
    
    private SweetService sweetService;

    @BeforeEach
    void setUp() {
        sweetService = new SweetService(sweetRepository, categoryRepository);
    }

    @Test
    void shouldCreateNewSweet() {
        // Given
        SweetDto sweetDto = new SweetDto(
            "Chocolate Bar", "Dark chocolate", new BigDecimal("5.99"), 10, 1L
        );
        
        Category category = new Category("Chocolate", "Chocolate sweets");
        Sweet savedSweet = new Sweet("Chocolate Bar", "Dark chocolate", new BigDecimal("5.99"), 10, category);
        savedSweet.setId(1L);
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(savedSweet);

        // When
        Sweet result = sweetService.createSweet(sweetDto);

        // Then
        assertNotNull(result);
        assertEquals("Chocolate Bar", result.getName());
        assertEquals(new BigDecimal("5.99"), result.getPrice());
        assertEquals(10, result.getQuantity());
        assertEquals(category, result.getCategory());
        
        verify(categoryRepository).findById(1L);
        verify(sweetRepository).save(any(Sweet.class));
    }

    @Test
    void shouldFindAllAvailableSweets() {
        // Given
        Sweet sweet1 = new Sweet("Sweet 1", "Description 1", new BigDecimal("5.99"), 10, new Category());
        Sweet sweet2 = new Sweet("Sweet 2", "Description 2", new BigDecimal("7.99"), 5, new Category());
        
        when(sweetRepository.findByQuantityGreaterThan(0)).thenReturn(Arrays.asList(sweet1, sweet2));

        // When
        List<Sweet> result = sweetService.findAvailableSweets();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.contains(sweet1));
        assertTrue(result.contains(sweet2));
        
        verify(sweetRepository).findByQuantityGreaterThan(0);
    }

    @Test
    void shouldPurchaseSweet() {
        // Given
        Long sweetId = 1L;
        int purchaseQuantity = 3;
        Sweet sweet = new Sweet("Sweet", "Description", new BigDecimal("5.99"), 10, new Category());
        sweet.setId(sweetId);
        
        when(sweetRepository.findById(sweetId)).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(sweet)).thenReturn(sweet);

        // When
        Sweet result = sweetService.purchaseSweet(sweetId, purchaseQuantity);

        // Then
        assertEquals(7, result.getQuantity()); // 10 - 3 = 7
        
        verify(sweetRepository).findById(sweetId);
        verify(sweetRepository).save(sweet);
    }

    @Test
    void shouldThrowExceptionWhenPurchasingMoreThanAvailable() {
        // Given
        Long sweetId = 1L;
        int purchaseQuantity = 15;
        Sweet sweet = new Sweet("Sweet", "Description", new BigDecimal("5.99"), 10, new Category());
        sweet.setId(sweetId);
        
        when(sweetRepository.findById(sweetId)).thenReturn(Optional.of(sweet));

        // When & Then
        assertThrows(InsufficientStockException.class, () -> {
            sweetService.purchaseSweet(sweetId, purchaseQuantity);
        });
        
        verify(sweetRepository).findById(sweetId);
        verify(sweetRepository, never()).save(any(Sweet.class));
    }

    @Test
    void shouldRestockSweet() {
        // Given
        Long sweetId = 1L;
        int restockQuantity = 20;
        Sweet sweet = new Sweet("Sweet", "Description", new BigDecimal("5.99"), 10, new Category());
        sweet.setId(sweetId);
        
        when(sweetRepository.findById(sweetId)).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(sweet)).thenReturn(sweet);

        // When
        Sweet result = sweetService.restockSweet(sweetId, restockQuantity);

        // Then
        assertEquals(30, result.getQuantity()); // 10 + 20 = 30
        
        verify(sweetRepository).findById(sweetId);
        verify(sweetRepository).save(sweet);
    }

    @Test
    void shouldSearchSweetsByCriteria() {
        // Given
        SweetSearchCriteria criteria = new SweetSearchCriteria();
        criteria.setName("chocolate");
        criteria.setMinPrice(new BigDecimal("5.00"));
        criteria.setMaxPrice(new BigDecimal("10.00"));
        
        Sweet sweet1 = new Sweet("Dark Chocolate", "Premium", new BigDecimal("8.99"), 10, new Category());
        Sweet sweet2 = new Sweet("Milk Chocolate", "Creamy", new BigDecimal("6.99"), 5, new Category());
        
        when(sweetRepository.findByNameContainingIgnoreCase("chocolate"))
            .thenReturn(Arrays.asList(sweet1, sweet2));

        // When
        List<Sweet> result = sweetService.searchSweets(criteria);

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(s -> 
            s.getName().toLowerCase().contains("chocolate") &&
            s.getPrice().compareTo(new BigDecimal("5.00")) >= 0 &&
            s.getPrice().compareTo(new BigDecimal("10.00")) <= 0
        ));
        
        verify(sweetRepository).findByNameContainingIgnoreCase("chocolate");
    }

    @Test
    void shouldThrowExceptionWhenSweetNotFound() {
        // Given
        Long nonExistentId = 999L;
        
        when(sweetRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(SweetNotFoundException.class, () -> {
            sweetService.findById(nonExistentId);
        });
        
        verify(sweetRepository).findById(nonExistentId);
    }
}
