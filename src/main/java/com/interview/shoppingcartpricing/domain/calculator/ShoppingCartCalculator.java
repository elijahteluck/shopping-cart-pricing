package com.interview.shoppingcartpricing.domain.calculator;

import com.interview.shoppingcartpricing.domain.cart.CartItem;
import com.interview.shoppingcartpricing.domain.cart.ShoppingCart;
import com.interview.shoppingcartpricing.domain.price.PricePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Domain service responsible for calculating the total price of a shopping cart.
 */
public final class ShoppingCartCalculator {

    private static final Logger log = LoggerFactory.getLogger(ShoppingCartCalculator.class);

    /**
     * Calculates the total price of the given shopping cart in EUR.
     *
     * @param cart the shopping cart
     * @return total price
     */
    public BigDecimal calculateTotal(ShoppingCart cart) {
        log.debug("Calculating total for client {} with {} items",
                cart.client().id(), cart.items().size());

        var pricePolicy = cart.client().pricePolicy();

        BigDecimal total = cart.items().stream()
                .map(item -> calculateLineTotal(pricePolicy, item))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.debug("Calculated total: {} EUR", total);
        return total;
    }

    private BigDecimal calculateLineTotal(PricePolicy pricePolicy, CartItem item) {
        BigDecimal unitPrice = pricePolicy.unitPrice(item.productType());
        return unitPrice.multiply(BigDecimal.valueOf(item.quantity()));
    }
}
