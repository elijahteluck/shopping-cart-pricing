package com.interview.shoppingcartpricing.domain.cart;

import com.interview.shoppingcartpricing.domain.product.ProductType;

/**
 * Represents a single line in the shopping cart with a product and a quantity.
 */
public record CartItem(
        ProductType productType,
        int quantity
) {

    public CartItem {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be strictly positive");
        }
    }
}
