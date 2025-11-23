package com.interview.shoppingcartpricing.adapter.inbound.web.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Request payload for a professional client.
 * Contains mandatory business identification fields.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfessionalClientRequest(
        @NotBlank String id,
        @NotBlank String companyName,
        String vatNumber,
        @NotBlank String registrationNumber,
        @NotNull BigDecimal annualRevenue
) implements ClientDto {}