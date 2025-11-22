package com.interview.shoppingcartpricing.domain.price;

import com.interview.shoppingcartpricing.domain.product.ProductType;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Price policy for professional clients with annual revenue >= 10M EUR.
 */
public final class ProfessionalHighRevenuePricePolicy implements PricePolicy {

    private static final Map<ProductType, BigDecimal> PRICES = new EnumMap<>(ProductType.class);
    public static final ProfessionalHighRevenuePricePolicy INSTANCE = new ProfessionalHighRevenuePricePolicy();

    static {
        PRICES.put(ProductType.HIGH_END_PHONE, BigDecimal.valueOf(1000));
        PRICES.put(ProductType.MID_RANGE_PHONE, BigDecimal.valueOf(550));
        PRICES.put(ProductType.LAPTOP, BigDecimal.valueOf(900));
    }

    @Override
    public BigDecimal unitPrice(ProductType productType) {
        return PRICES.get(productType);
    }
}
