package com.interview.shoppingcartpricing.domain.cart;

import com.interview.shoppingcartpricing.domain.client.Client;

import java.util.List;
import java.util.Objects;

/**
 * Aggregate root representing a shopping cart for a specific client.
 */
public record ShoppingCart(
        Client client,
        List<CartItem> items
) {

    public ShoppingCart {
        Objects.requireNonNull(client, "client must not be null");
        Objects.requireNonNull(items, "items must not be null");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Shopping cart must contain at least one item");
        }
    }
}
