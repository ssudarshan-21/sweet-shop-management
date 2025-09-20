package com.sweetshop.service;

import com.sweetshop.entity.Sweet;
import com.sweetshop.entity.Category;
import com.sweetshop.repository.SweetRepository;
import com.sweetshop.repository.CategoryRepository;
import com.sweetshop.dto.SweetDto;
import com.sweetshop.dto.SweetSearchCriteria;
import com.sweetshop.exception.SweetNotFoundException;
import com.sweetshop.exception.InsufficientStockException;
import com.sweetshop.exception.CategoryNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SweetService {

    private final SweetRepository sweetRepository;
    private final CategoryRepository categoryRepository;

    public SweetService(SweetRepository sweetRepository, CategoryRepository categoryRepository) {
        this.sweetRepository = sweetRepository;
        this.categoryRepository = categoryRepository;
    }

    public Sweet createSweet(SweetDto sweetDto) {
        Category category = categoryRepository.findById(sweetDto.getCategoryId())
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + sweetDto.getCategoryId()));

        Sweet sweet = new Sweet();
        sweet.setName(sweetDto.getName());
        sweet.setDescription(sweetDto.getDescription());
        sweet.setPrice(sweetDto.getPrice());
        sweet.setQuantity(sweetDto.getQuantity());
        sweet.setCategory(category);
        sweet.setImageUrl(sweetDto.getImageUrl());

        return sweetRepository.save(sweet);
    }

    @Transactional(readOnly = true)
    public List<Sweet> findAllSweets() {
        return sweetRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Sweet> findAvailableSweets() {
        return sweetRepository.findByQuantityGreaterThan(0);
    }

    @Transactional(readOnly = true)
    public Sweet findById(Long id) {
        return sweetRepository.findById(id)
            .orElseThrow(() -> new SweetNotFoundException("Sweet not found with id: " + id));
    }

    public Sweet updateSweet(Long id, SweetDto sweetDto) {
        Sweet sweet = findById(id);
        
        sweet.setName(sweetDto.getName());
        sweet.setDescription(sweetDto.getDescription());
        sweet.setPrice(sweetDto.getPrice());
        sweet.setQuantity(sweetDto.getQuantity());
        sweet.setImageUrl(sweetDto.getImageUrl());

        if (sweetDto.getCategoryId() != null && !sweetDto.getCategoryId().equals(sweet.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(sweetDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + sweetDto.getCategoryId()));
            sweet.setCategory(newCategory);
        }

        return sweetRepository.save(sweet);
    }

    public void deleteSweet(Long id) {
        Sweet sweet = findById(id);
        sweetRepository.delete(sweet);
    }

    public Sweet purchaseSweet(Long sweetId, int quantity) {
        Sweet sweet = findById(sweetId);
        
        if (sweet.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough stock available. Available: " + sweet.getQuantity() + ", Requested: " + quantity);
        }

        sweet.decreaseQuantity(quantity);
        return sweetRepository.save(sweet);
    }

    public Sweet restockSweet(Long sweetId, int quantity) {
        Sweet sweet = findById(sweetId);
        sweet.increaseQuantity(quantity);
        return sweetRepository.save(sweet);
    }

    @Transactional(readOnly = true)
    public List<Sweet> searchSweets(SweetSearchCriteria criteria) {
        List<Sweet> sweets;

        if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
            sweets = sweetRepository.findByNameContainingIgnoreCase(criteria.getName());
        } else if (criteria.getCategoryId() != null) {
            Category category = categoryRepository.findById(criteria.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
            sweets = sweetRepository.findByCategory(category);
        } else {
            sweets = sweetRepository.findAll();
        }

        // Apply price filtering
        if (criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
            sweets = sweets.stream()
                .filter(sweet -> {
                    BigDecimal price = sweet.getPrice();
                    boolean withinRange = true;
                    
                    if (criteria.getMinPrice() != null) {
                        withinRange = price.compareTo(criteria.getMinPrice()) >= 0;
                    }
                    
                    if (withinRange && criteria.getMaxPrice() != null) {
                        withinRange = price.compareTo(criteria.getMaxPrice()) <= 0;
                    }
                    
                    return withinRange;
                })
                .collect(Collectors.toList());
        }

        // Apply availability filter
        if (criteria.isOnlyAvailable()) {
            sweets = sweets.stream()
                .filter(Sweet::isAvailable)
                .collect(Collectors.toList());
        }

        return sweets;
    }

    @Transactional(readOnly = true)
    public List<Sweet> findSweetsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
        return sweetRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Sweet> findTopSellingSweets(int limit) {
        return sweetRepository.findTopSellingSweets(PageRequest.of(0, limit));
    }

    @Transactional(readOnly = true)
    public List<Sweet> findLowStockSweets(int threshold) {
        return sweetRepository.findLowStockSweets(threshold);
    }

    @Transactional(readOnly = true)
    public List<Sweet> findOutOfStockSweets() {
        return sweetRepository.findOutOfStockSweets();
    }
}