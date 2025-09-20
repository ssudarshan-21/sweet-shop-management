package com.sweetshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 255)
    @NotBlank(message = "Token is required")
    private String token;
    
    @Column(name = "expiry_date", nullable = false)
    @NotNull(message = "Expiry date is required")
    private LocalDateTime expiryDate;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Constructors
    public RefreshToken() {}
    
    public RefreshToken(String token, User user, LocalDateTime expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    // Business Logic
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
    
    public void extend(int days) {
        this.expiryDate = LocalDateTime.now().plusDays(days);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RefreshToken that = (RefreshToken) obj;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", token='" + token.substring(0, Math.min(token.length(), 10)) + "..." + '\'' +
                ", expiryDate=" + expiryDate +
                ", expired=" + isExpired() +
                '}';
    }
}

