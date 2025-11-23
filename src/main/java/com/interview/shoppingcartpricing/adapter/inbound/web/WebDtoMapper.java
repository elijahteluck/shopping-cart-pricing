package com.interview.shoppingcartpricing.adapter.inbound.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.*;
import com.interview.shoppingcartpricing.application.CartCalculationCommand;
import com.interview.shoppingcartpricing.application.CartCalculationResult;
import com.interview.shoppingcartpricing.domain.cart.CartItem;
import com.interview.shoppingcartpricing.domain.client.Client;
import com.interview.shoppingcartpricing.domain.client.IndividualClient;
import com.interview.shoppingcartpricing.domain.client.ProfessionalClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Maps web-layer DTOs into application-layer commands and vice versa.
 */
@Component
@RequiredArgsConstructor
public class WebDtoMapper {

    private final ObjectMapper objectMapper;
    private final ClientDtoValidatorService validator;

    public CartCalculationCommand toCommand(CartCalculationRequest request) {
        Client client = mapClient(request.client());

        List<CartItem> items = request.items()
                .stream()
                .map(i -> new CartItem(i.productType(), i.quantity()))
                .toList();

        return new CartCalculationCommand(client, items);
    }

    /**
     * Detects client type from the raw JSON object and maps it
     * to the appropriate domain model representation.
     */
    private Client mapClient(Object raw) {

        if (!(raw instanceof Map<?, ?> map)) {
            throw new IllegalArgumentException("Client body must be a JSON object");
        }

        Object typeValue = map.get("type");
        if (typeValue == null) {
            throw new IllegalArgumentException("Missing required field: client.type");
        }

        String type = typeValue.toString().toUpperCase();

        return switch (type) {
            case "INDIVIDUAL" -> mapIndividual(map);
            case "PROFESSIONAL" -> mapProfessional(map);
            default -> throw new IllegalArgumentException("Unsupported client type: " + type);
        };
    }

    private Client mapIndividual(Map<?, ?> raw) {
        IndividualClientRequest dto = validator.validate(
                objectMapper.convertValue(raw, IndividualClientRequest.class)
        );

        return new IndividualClient(dto.id(), dto.firstName(), dto.lastName());
    }

    private Client mapProfessional(Map<?, ?> raw) {
        ProfessionalClientRequest dto = validator.validate(
                objectMapper.convertValue(raw, ProfessionalClientRequest.class)
        );

        return new ProfessionalClient(
                dto.id(),
                dto.companyName(),
                dto.vatNumber(),
                dto.registrationNumber(),
                dto.annualRevenue()
        );
    }

    public CartCalculationResponse toResponse(CartCalculationResult result) {
        return new CartCalculationResponse(
                result.total(),
                result.currency().name()
        );
    }
}
