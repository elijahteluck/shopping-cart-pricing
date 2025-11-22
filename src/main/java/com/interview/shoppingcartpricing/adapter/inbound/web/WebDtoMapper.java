package com.interview.shoppingcartpricing.adapter.inbound.web;

import com.interview.shoppingcartpricing.application.CartCalculationCommand;
import com.interview.shoppingcartpricing.application.CartCalculationResult;
import com.interview.shoppingcartpricing.domain.cart.CartItem;
import com.interview.shoppingcartpricing.domain.client.Client;
import com.interview.shoppingcartpricing.domain.client.IndividualClient;
import com.interview.shoppingcartpricing.domain.client.ProfessionalClient;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationResponse;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartItemRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.ClientRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper responsible for translating between HTTP DTOs and domain/application objects.
 */
@Component
public final class WebDtoMapper {

    private WebDtoMapper() {
        // Utility class
    }

    public CartCalculationCommand toCommand(CartCalculationRequest request) {
        Client client = toClient(request.client());
        List<CartItem> items = request.items().stream()
                .map(this::toCartItem)
                .toList();
        return new CartCalculationCommand(client, items);
    }

    public CartCalculationResponse toResponse(CartCalculationResult result) {
        return new CartCalculationResponse(result.total(), result.currency().name());
    }

    private Client toClient(ClientRequest request) {
        String type = request.type().toUpperCase();

        return switch (type) {
            case "INDIVIDUAL" -> new IndividualClient(
                    request.id(),
                    request.firstName(),
                    request.lastName()
            );
            case "PROFESSIONAL" -> new ProfessionalClient(
                    request.id(),
                    request.companyName(),
                    request.vatNumber(),
                    request.registrationNumber(),
                    defaultAnnualRevenue(request.annualRevenue())
            );
            default -> throw new IllegalArgumentException("Unsupported client type: " + type);
        };
    }

    private BigDecimal defaultAnnualRevenue(BigDecimal revenue) {
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    private CartItem toCartItem(CartItemRequest request) {
        return new CartItem(request.productType(), request.quantity());
    }
}
