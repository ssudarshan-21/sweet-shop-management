package com.sweetshop.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class SweetDto {
    
    @NotBlank(message = "Sweet name is required")
    @Size(max = 100, message = "Sweet name must be less than 100 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits and 2 decimal places")
    private BigDecimal price;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    @NotNull(message = "Category is required")
    private Long categoryId;
    
    private String imageUrl;

    public SweetDto() {}

    public SweetDto(String name, String description, BigDecimal price, Integer quantity, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}