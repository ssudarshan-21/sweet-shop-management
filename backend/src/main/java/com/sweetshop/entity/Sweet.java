package com.sweetshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "sweets")
public class Sweet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "Sweet name is required")
    @Size(max = 100, message = "Sweet name must be less than 100 characters")
    private String name;
    
    @Column(length = 1000)
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits and 2 decimal places")
    private BigDecimal price;
    
    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
    
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    
    @ManyToOne(fetch = FetchType.EAGER) // <-- Change LAZY to EAGER
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required")
    private Category category;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Sweet() {}
    
    public Sweet(String name, String description, BigDecimal price, Integer quantity, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    // Business Logic
    public boolean isAvailable() {
        return quantity != null && quantity > 0;
    }
    
    public void decreaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (quantity < amount) {
            throw new IllegalArgumentException("Not enough quantity available");
        }
        this.quantity -= amount;
    }
    
    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.quantity += amount;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sweet sweet = (Sweet) obj;
        return id != null && id.equals(sweet.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Sweet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", available=" + isAvailable() +
                '}';
    }
}
