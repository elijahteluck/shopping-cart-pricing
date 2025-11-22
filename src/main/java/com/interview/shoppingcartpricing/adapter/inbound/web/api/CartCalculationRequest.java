package com.interview.shoppingcartpricing.adapter.inbound.web.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * DTO representing the payload to calculate a cart total.
 */
public record CartCalculationRequest(
        @NotNull @Valid ClientRequest client,
        @NotNull @Valid List<CartItemRequest> items
) {
}
