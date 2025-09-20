package com.sweetshop.repository;

import com.sweetshop.entity.Sweet;
import com.sweetshop.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SweetRepository extends JpaRepository<Sweet, Long> {
    
    List<Sweet> findByCategory(Category category);
    
    List<Sweet> findByQuantityGreaterThan(Integer quantity);
    
    List<Sweet> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Sweet> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT s FROM Sweet s WHERE s.category.name = :categoryName")
    List<Sweet> findByCategoryName(@Param("categoryName") String categoryName);
    
    @Query("SELECT s FROM Sweet s WHERE s.name LIKE %:name% OR s.description LIKE %:description%")
    List<Sweet> searchByNameOrDescription(@Param("name") String name, @Param("description") String description);
    
    @Query("SELECT s FROM Sweet s WHERE s.price BETWEEN :minPrice AND :maxPrice AND s.category.name = :categoryName")
    List<Sweet> findByPriceRangeAndCategory(
        @Param("minPrice") BigDecimal minPrice, 
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("categoryName") String categoryName
    );
    
    @Query("SELECT s FROM Sweet s ORDER BY s.quantity DESC")
    List<Sweet> findTopSellingSweets(Pageable pageable);
    
    @Query("SELECT s FROM Sweet s WHERE s.quantity <= :threshold AND s.quantity > 0 ORDER BY s.quantity ASC")
    List<Sweet> findLowStockSweets(@Param("threshold") Integer threshold);
    
    @Query("SELECT s FROM Sweet s WHERE s.quantity = 0")
    List<Sweet> findOutOfStockSweets();
    
    @Query("SELECT s FROM Sweet s ORDER BY s.createdAt DESC")
    List<Sweet> findNewestSweets(Pageable pageable);
    
    @Query("SELECT s FROM Sweet s ORDER BY s.price ASC")
    List<Sweet> findCheapestSweets(Pageable pageable);
    
    @Query("SELECT COUNT(s) FROM Sweet s WHERE s.category = :category")
    Long countByCategory(@Param("category") Category category);
}