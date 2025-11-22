package com.interview.shoppingcartpricing.application;

import com.interview.shoppingcartpricing.domain.common.Currency;

import java.math.BigDecimal;

/**
 * Result object representing the outcome of a cart total calculation.
 */
public record CartCalculationResult(
        BigDecimal total,
        Currency currency
) {
}
