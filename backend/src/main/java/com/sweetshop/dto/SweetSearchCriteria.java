package com.sweetshop.dto;

import java.math.BigDecimal;

public class SweetSearchCriteria {
    
    private String name;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private boolean onlyAvailable = true;

    public SweetSearchCriteria() {}

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }

    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public boolean isOnlyAvailable() { return onlyAvailable; }
    public void setOnlyAvailable(boolean onlyAvailable) { this.onlyAvailable = onlyAvailable; }
}
