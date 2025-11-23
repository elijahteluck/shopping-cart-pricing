package com.interview.shoppingcartpricing.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.shoppingcartpricing.ShoppingCartPricingApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the shopping cart pricing REST API.
 * Verifies correct behavior for both client types and error scenarios.
 */
@SpringBootTest(classes = ShoppingCartPricingApplication.class)
@AutoConfigureMockMvc
class CartControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // Individual client
    @Test
    void shouldCalculateTotalForIndividualClient() throws Exception {
        var client = Map.of(
                "type", "INDIVIDUAL",
                "id", "C1",
                "firstName", "John",
                "lastName", "Doe"
        );

        var items = List.of(
                Map.of("productType", "LAPTOP", "quantity", 1)
        );

        var request = Map.of(
                "client", client,
                "items", items
        );

        mockMvc.perform(
                        post("/api/carts/total")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.total", is(1200))); // laptop individual price
    }


    // Professional client (high revenue)
    @Test
    void shouldCalculateTotalForProfessionalHighRevenue() throws Exception {
        var client = Map.of(
                "type", "PROFESSIONAL",
                "id", "P1",
                "companyName", "Big Corp",
                "registrationNumber", "REG-001",
                "annualRevenue", BigDecimal.valueOf(15_000_000)
        );

        var items = List.of(
                Map.of("productType", "HIGH_END_PHONE", "quantity", 2)
        );

        var request = Map.of(
                "client", client,
                "items", items
        );

        mockMvc.perform(
                        post("/api/carts/total")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.total", is(2000))); // 2 * 1000 (high revenue price)
    }

    // Professional client (low revenue)
    @Test
    void shouldCalculateTotalForProfessionalLowRevenue() throws Exception {

        var client = Map.of(
                "type", "PROFESSIONAL",
                "id", "P2",
                "companyName", "Small Biz",
                "registrationNumber", "REG-002",
                "annualRevenue", BigDecimal.valueOf(5_000_000)  // <= 10M
        );

        var items = List.of(
                Map.of("productType", "MID_RANGE_PHONE", "quantity", 1),  // 600
                Map.of("productType", "LAPTOP", "quantity", 2)            // 2 × 1000 = 2000
        );

        var request = Map.of(
                "client", client,
                "items", items
        );

        mockMvc.perform(
                        post("/api/carts/total")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.total", is(2600))); // 600 + 2000
    }


    // Invalid professional client
    @Test
    void shouldReturnBadRequestForMissingRequiredProfessionalFields() throws Exception {
        // Missing registrationNumber AND missing annualRevenue
        var client = Map.of(
                "type", "PROFESSIONAL",
                "id", "P1",
                "companyName", "Big Corp"
        );

        var request = Map.of(
                "client", client,
                "items", List.of(
                        Map.of("productType", "LAPTOP", "quantity", 1)
                )
        );

        mockMvc.perform(
                        post("/api/carts/total")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors").exists());
    }

    // Invalid product type
    @Test
    void shouldReturnBadRequestForUnknownProductType() throws Exception {

        var unsupportedType = "Tetris";
        var client = Map.of(
                "id", "C1",
                "firstName", "John",
                "lastName", "Doe"
        );

        var badItem = Map.of(
                "productType", unsupportedType,  // Unsupported type
                "quantity", 1
        );

        var request = Map.of(
                "clientType", "INDIVIDUAL",
                "client", client,
                "items", List.of(badItem)
        );

        mockMvc.perform(
                        post("/api/carts/total")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Unsupported product type: " + unsupportedType)));
    }
}
