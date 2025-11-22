package com.interview.shoppingcartpricing.domain.client;


import com.interview.shoppingcartpricing.domain.price.IndividualPricePolicy;
import com.interview.shoppingcartpricing.domain.price.PricePolicy;

/**
 * Represents an individual client identified by first and last name.
 */
public record IndividualClient(
        String id,
        String firstName,
        String lastName
) implements Client {

    @Override
    public PricePolicy pricePolicy() {
        return IndividualPricePolicy.INSTANCE;
    }
}
