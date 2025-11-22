package com.interview.shoppingcartpricing.adapter.inbound.web.api;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * DTO representing a client coming from the HTTP layer.
 * <p>
 * "type" determines which client subtype will be constructed.
 * Supported values: "INDIVIDUAL", "PROFESSIONAL".
 */
public record ClientRequest(
        @NotBlank String type,
        @NotBlank String id,
        String firstName,
        String lastName,
        String companyName,
        String vatNumber,
        String registrationNumber,
        BigDecimal annualRevenue
) {
}
