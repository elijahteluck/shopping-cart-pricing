package com.interview.shoppingcartpricing.adapter.inbound.web.api;

import java.math.BigDecimal;

/**
 * DTO representing the response of a cart total calculation.
 */
public record CartCalculationResponse(
        BigDecimal total,
        String currency
) {
}
