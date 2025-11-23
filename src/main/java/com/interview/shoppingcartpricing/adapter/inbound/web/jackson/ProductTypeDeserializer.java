package com.interview.shoppingcartpricing.adapter.inbound.web.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.interview.shoppingcartpricing.domain.product.ProductType;

import java.io.IOException;

/**
 * Custom deserializer that converts a string into ProductType.
 * Throws a meaningful validation error when the value is unknown.
 */
public class ProductTypeDeserializer extends JsonDeserializer<ProductType> {

    @Override
    public ProductType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();

        try {
            return ProductType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unsupported product type: " + value);
        }
    }
}
