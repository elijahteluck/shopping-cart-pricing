package com.interview.shoppingcartpricing.domain.client;


import com.interview.shoppingcartpricing.domain.price.PricePolicy;

/**
 * Marker type for all clients.
 * <p>
 * This sealed hierarchy makes it explicit which client types are supported.
 */
public sealed interface Client permits IndividualClient, ProfessionalClient {

    String id();
    PricePolicy pricePolicy();
}
