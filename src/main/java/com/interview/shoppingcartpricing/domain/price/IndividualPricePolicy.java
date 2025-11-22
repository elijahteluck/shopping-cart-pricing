package com.interview.shoppingcartpricing.domain.price;

import com.interview.shoppingcartpricing.domain.product.ProductType;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Price policy for individual clients.
 */
public final class IndividualPricePolicy implements PricePolicy {

    private static final Map<ProductType, BigDecimal> PRICES = new EnumMap<>(ProductType.class);
    public static final IndividualPricePolicy INSTANCE = new IndividualPricePolicy();


    static {
        PRICES.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1500));
        PRICES.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(800));
        PRICES.put(ProductType.LAPTOP, BigDecimal.valueOf(1200));
    }

    @Override
    public BigDecimal unitPrice(ProductType productType) {
        return PRICES.get(productType);
    }
}
