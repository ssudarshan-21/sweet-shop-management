package com.sweetshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sweet_id", nullable = false)
    @NotNull
    private Sweet sweet;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal totalAmount;
    
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }

    // Constructors
    public Order() {}

    public Order(User user, Sweet sweet, Integer quantity, BigDecimal totalAmount) {
        this.user = user;
        this.sweet = sweet;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Sweet getSweet() { return sweet; }
    public void setSweet(Sweet sweet) { this.sweet = sweet; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                '}';
    }
}