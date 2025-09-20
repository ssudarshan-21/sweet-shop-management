package com.sweetshop.controller;

import com.sweetshop.service.SweetService;
import com.sweetshop.entity.Sweet;
import com.sweetshop.entity.Category;
import com.sweetshop.dto.SweetDto;
import com.sweetshop.dto.PurchaseRequest;
import com.sweetshop.exception.SweetNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SweetController.class)
@ActiveProfiles("test")
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    
	@MockBean
    private SweetService sweetService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sweet testSweet;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category("Chocolate", "Chocolate sweets");
        testCategory.setId(1L);
        
        testSweet = new Sweet("Dark Chocolate", "Premium dark chocolate", new BigDecimal("8.99"), 10, testCategory);
        testSweet.setId(1L);
    }

    @Test
    void shouldGetAllSweets() throws Exception {
        // Given
        List<Sweet> sweets = Arrays.asList(testSweet);
        when(sweetService.findAvailableSweets()).thenReturn(sweets);

        // When & Then
        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Dark Chocolate"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateSweetAsAdmin() throws Exception {
        // Given
        SweetDto sweetDto = new SweetDto("New Sweet", "Description", new BigDecimal("5.99"), 20, 1L);
        
        when(sweetService.createSweet(any(SweetDto.class))).thenReturn(testSweet);

        // When & Then
        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sweetDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Dark Chocolate"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbidCreateSweetAsUser() throws Exception {
        // Given
        SweetDto sweetDto = new SweetDto("New Sweet", "Description", new BigDecimal("5.99"), 20, 1L);

        // When & Then
        mockMvc.perform(post("/api/sweets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sweetDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldPurchaseSweetAsUser() throws Exception {
        // Given
        PurchaseRequest purchaseRequest = new PurchaseRequest(2);
        Sweet updatedSweet = new Sweet("Dark Chocolate", "Premium dark chocolate", new BigDecimal("8.99"), 8, testCategory);
        updatedSweet.setId(1L);
        
        when(sweetService.purchaseSweet(1L, 2)).thenReturn(updatedSweet);

        // When & Then
        mockMvc.perform(post("/api/sweets/1/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.quantity").value(8));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldRestockSweetAsAdmin() throws Exception {
        // Given
        PurchaseRequest restockRequest = new PurchaseRequest(10);
        Sweet restockedSweet = new Sweet("Dark Chocolate", "Premium dark chocolate", new BigDecimal("8.99"), 20, testCategory);
        restockedSweet.setId(1L);
        
        when(sweetService.restockSweet(1L, 10)).thenReturn(restockedSweet);

        // When & Then
        mockMvc.perform(post("/api/sweets/1/restock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.quantity").value(20));
    }

    @Test
    void shouldReturnNotFoundForInvalidSweet() throws Exception {
        // Given
        when(sweetService.findById(999L)).thenThrow(new SweetNotFoundException("Sweet not found"));

        // When & Then
        mockMvc.perform(get("/api/sweets/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }
}
