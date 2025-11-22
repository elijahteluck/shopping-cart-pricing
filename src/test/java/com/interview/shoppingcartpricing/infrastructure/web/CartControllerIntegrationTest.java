package com.interview.shoppingcartpricing.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.shoppingcartpricing.ShoppingCartPricingApplication;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartItemRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.ClientRequest;
import com.interview.shoppingcartpricing.domain.product.ProductType;
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
 * Integration test verifying the REST endpoint end-to-end.
 */
@SpringBootTest(classes = ShoppingCartPricingApplication.class)
@AutoConfigureMockMvc
class CartControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldCalculateTotalForIndividualClientViaHttp() throws Exception {
        Map<String, Object> client = Map.of(
                "type", "INDIVIDUAL",
                "id", "C1",
                "firstName", "John",
                "lastName", "Doe"
        );

        List<Map<String, Object>> items = List.of(
                Map.of("productType", "HIGH_END_PHONE", "quantity", 1),
                Map.of("productType", "MID_RANGE_PHONE", "quantity", 1)
        );

        Map<String, Object> requestBody = Map.of(
                "client", client,
                "items", items
        );

        mockMvc.perform(post("/api/carts/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.total", is(2300)));
    }

    @Test
    void shouldCalculateTotalForHighRevenueProfessionalClientViaHttp() throws Exception {
        Map<String, Object> client = Map.of(
                "type", "PROFESSIONAL",
                "id", "P1",
                "companyName", "Big Corp",
                "annualRevenue", BigDecimal.valueOf(15_000_000L)
        );

        List<Map<String, Object>> items = List.of(
                Map.of("productType", "LAPTOP", "quantity", 2)
        );

        Map<String, Object> requestBody = Map.of(
                "client", client,
                "items", items
        );

        mockMvc.perform(post("/api/carts/total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.total", is(1800)));
    }

    @Test
    void shouldReturnBadRequestForInvalidInput() throws Exception {
        ClientRequest client = new ClientRequest(
                "INDIVIDUAL", "C1", "John", "Doe",
                null, null, null, null
        );

        CartCalculationRequest request = new CartCalculationRequest(
                client,
                List.of(new CartItemRequest(ProductType.HIGH_END_PHONE, 0))
        );

        mockMvc.perform(
                        post("/api/carts/total")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }
}
