package com.interview.shoppingcartpricing.domain.price;

import com.interview.shoppingcartpricing.domain.product.ProductType;

import java.math.BigDecimal;

/**
 * Price policy abstraction that provides the unit price per product type.
 */
public interface PricePolicy {

    /**
     * Returns the unit price for a given product type.
     *
     * @param productType the product type
     * @return unit price in EUR
     */
    BigDecimal unitPrice(ProductType productType);
}
