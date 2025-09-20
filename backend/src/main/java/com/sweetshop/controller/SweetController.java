package com.sweetshop.controller;

import com.sweetshop.service.SweetService;
import com.sweetshop.entity.Sweet;
import com.sweetshop.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sweets")
@CrossOrigin(origins = "*")
public class SweetController {

    @Autowired
    private SweetService sweetService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Sweet>>> getAllSweets() {
        List<Sweet> sweets = sweetService.findAvailableSweets();
        return ResponseEntity.ok(ApiResponse.success("Sweets retrieved successfully", sweets));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Sweet>> getSweetById(@PathVariable Long id) {
        Sweet sweet = sweetService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Sweet retrieved successfully", sweet));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Sweet>>> searchSweets(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "true") boolean onlyAvailable) {
        
        SweetSearchCriteria criteria = new SweetSearchCriteria();
        criteria.setName(name);
        criteria.setCategoryId(categoryId);
        criteria.setMinPrice(minPrice);
        criteria.setMaxPrice(maxPrice);
        criteria.setOnlyAvailable(onlyAvailable);
        
        List<Sweet> sweets = sweetService.searchSweets(criteria);
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved successfully", sweets));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Sweet>> createSweet(@Valid @RequestBody SweetDto sweetDto) {
        Sweet sweet = sweetService.createSweet(sweetDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("Sweet created successfully", sweet));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Sweet>> updateSweet(@PathVariable Long id, @Valid @RequestBody SweetDto sweetDto) {
        Sweet sweet = sweetService.updateSweet(id, sweetDto);
        return ResponseEntity.ok(ApiResponse.success("Sweet updated successfully", sweet));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteSweet(@PathVariable Long id) {
        sweetService.deleteSweet(id);
        return ResponseEntity.ok(ApiResponse.success("Sweet deleted successfully", "Sweet deleted"));
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<ApiResponse<Sweet>> purchaseSweet(@PathVariable Long id, 
                                                           @Valid @RequestBody PurchaseRequest request) {
        Sweet sweet = sweetService.purchaseSweet(id, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Sweet purchased successfully", sweet));
    }

    @PostMapping("/{id}/restock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Sweet>> restockSweet(@PathVariable Long id, 
                                                          @Valid @RequestBody PurchaseRequest request) {
        Sweet sweet = sweetService.restockSweet(id, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Sweet restocked successfully", sweet));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<Sweet>>> getSweetsByCategory(@PathVariable Long categoryId) {
        List<Sweet> sweets = sweetService.findSweetsByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Sweets by category retrieved successfully", sweets));
    }

    @GetMapping("/top-selling")
    public ResponseEntity<ApiResponse<List<Sweet>>> getTopSellingSweets(@RequestParam(defaultValue = "10") int limit) {
        List<Sweet> sweets = sweetService.findTopSellingSweets(limit);
        return ResponseEntity.ok(ApiResponse.success("Top selling sweets retrieved successfully", sweets));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Sweet>>> getLowStockSweets(@RequestParam(defaultValue = "5") int threshold) {
        List<Sweet> sweets = sweetService.findLowStockSweets(threshold);
        return ResponseEntity.ok(ApiResponse.success("Low stock sweets retrieved successfully", sweets));
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Sweet>>> getOutOfStockSweets() {
        List<Sweet> sweets = sweetService.findOutOfStockSweets();
        return ResponseEntity.ok(ApiResponse.success("Out of stock sweets retrieved successfully", sweets));
    }
}
