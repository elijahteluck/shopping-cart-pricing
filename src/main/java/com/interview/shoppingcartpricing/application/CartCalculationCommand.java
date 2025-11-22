package com.interview.shoppingcartpricing.application;

import com.interview.shoppingcartpricing.domain.cart.CartItem;
import com.interview.shoppingcartpricing.domain.client.Client;

import java.util.List;

/**
 * Command object representing a request to calculate a cart total.
 */
public record CartCalculationCommand(
        Client client,
        List<CartItem> items
) {
}
