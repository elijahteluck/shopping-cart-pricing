package com.interview.shoppingcartpricing.adapter.inbound.web.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * Request wrapper encapsulating client information and cart items.
 * Allows polymorphic client types via {@link ClientDto}.
 */
public record CartCalculationRequest(
        @Valid Object client,
        @NotEmpty List<CartItemRequest> items
) {}
