package com.interview.shoppingcartpricing.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.shoppingcartpricing.adapter.inbound.web.ClientDtoValidatorService;
import com.interview.shoppingcartpricing.adapter.inbound.web.WebDtoMapper;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.*;
import com.interview.shoppingcartpricing.application.CartCalculationCommand;
import com.interview.shoppingcartpricing.application.CartCalculationResult;
import com.interview.shoppingcartpricing.domain.client.Client;
import com.interview.shoppingcartpricing.domain.client.IndividualClient;
import com.interview.shoppingcartpricing.domain.client.ProfessionalClient;
import com.interview.shoppingcartpricing.domain.common.Currency;
import com.interview.shoppingcartpricing.domain.product.ProductType;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Unit tests for {@link WebDtoMapper}, verifying JSON to DTO to Domain mapping
 * for both individual and professional client request formats.
 *
 * This test works on raw Map-based JSON structures to accurately simulate
 * real controller input, matching the behavior of ObjectMapper.convertValue().
 */
class WebDtoMapperTest {

    private WebDtoMapper mapper;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        mapper = new WebDtoMapper(
                new ObjectMapper(),
                new ClientDtoValidatorService(validator)
        );
    }

    @Test
    void shouldMapIndividualClient() {
        // given
        IndividualClientRequest individual = new IndividualClientRequest(
                "C1",
                "John",
                "Doe"
        );

        CartCalculationRequest request = new CartCalculationRequest(
                individualToRaw(individual),
                List.of(new CartItemRequest(ProductType.LAPTOP, 1))
        );

        // when
        CartCalculationCommand command = mapper.toCommand(request);
        Client client = command.client();

        // then
        assertTrue(client instanceof IndividualClient);

        IndividualClient c = (IndividualClient) client;
        assertEquals("C1", c.id());
        assertEquals("John", c.firstName());
        assertEquals("Doe", c.lastName());
    }

    @Test
    void shouldMapProfessionalClient() {
        // given
        ProfessionalClientRequest professional = new ProfessionalClientRequest(
                "P1",
                "Big Corp",
                "VAT123",
                "REG-45",
                BigDecimal.valueOf(15_000_000)
        );

        CartCalculationRequest request = new CartCalculationRequest(
                professionalToRaw(professional),
                List.of(new CartItemRequest(ProductType.HIGH_END_PHONE, 2))
        );

        // when
        CartCalculationCommand command = mapper.toCommand(request);
        Client client = command.client();

        // then
        assertTrue(client instanceof ProfessionalClient);

        ProfessionalClient c = (ProfessionalClient) client;
        assertEquals("P1", c.id());
        assertEquals("Big Corp", c.companyName());
        assertEquals("VAT123", c.vatNumber());
        assertEquals("REG-45", c.registrationNumber());
        assertEquals(BigDecimal.valueOf(15_000_000), c.annualRevenue());
    }

    @Test
    void shouldMapResponse() {
        // given
        CartCalculationResult result = new CartCalculationResult(
                BigDecimal.valueOf(2500),
                Currency.EUR
        );

        // when
        CartCalculationResponse response = mapper.toResponse(result);

        // then
        assertEquals(BigDecimal.valueOf(2500), response.total());
        assertEquals("EUR", response.currency());
    }

    // helper methods
    private Map<String, Object> individualToRaw(IndividualClientRequest dto) {
        return Map.of(
                "type", "INDIVIDUAL",
                "id", dto.id(),
                "firstName", dto.firstName(),
                "lastName", dto.lastName()
        );
    }

    private Map<String, Object> professionalToRaw(ProfessionalClientRequest dto) {
        return Map.of(
                "type", "PROFESSIONAL",
                "id", dto.id(),
                "companyName", dto.companyName(),
                "vatNumber", dto.vatNumber(),
                "registrationNumber", dto.registrationNumber(),
                "annualRevenue", dto.annualRevenue()
        );
    }
}
