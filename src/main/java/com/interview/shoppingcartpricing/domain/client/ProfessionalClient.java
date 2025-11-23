package com.interview.shoppingcartpricing.domain.client;

import com.interview.shoppingcartpricing.domain.price.PricePolicy;
import com.interview.shoppingcartpricing.domain.price.ProfessionalHighRevenuePricePolicy;
import com.interview.shoppingcartpricing.domain.price.ProfessionalLowRevenuePricePolicy;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a professional client identified by company information.
 */
public record ProfessionalClient(
        String id,
        String companyName,
        String vatNumber,
        String registrationNumber,
        BigDecimal annualRevenue
) implements Client {

    private static final BigDecimal HIGH_REVENUE_THRESHOLD = BigDecimal.valueOf(10_000_000L);

    public ProfessionalClient {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(companyName, "companyName must not be null");
        Objects.requireNonNull(registrationNumber, "registrationNumber must not be null");
        Objects.requireNonNull(annualRevenue, "annualRevenue must not be null");

        if (annualRevenue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("annualRevenue must not be negative");
        }
    }

    @Override
    public PricePolicy pricePolicy() {
        if (annualRevenue().compareTo(HIGH_REVENUE_THRESHOLD) > 0) {
            return ProfessionalHighRevenuePricePolicy.INSTANCE;
        }
        return ProfessionalLowRevenuePricePolicy.INSTANCE;
    }
}
