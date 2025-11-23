package com.interview.shoppingcartpricing.adapter.inbound.web.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for an individual client.
 * Contains only fields relevant to individual customers.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record IndividualClientRequest(
        @NotBlank String id,
        @NotBlank String firstName,
        @NotBlank String lastName
) implements ClientDto {}
