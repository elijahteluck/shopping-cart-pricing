package com.interview.shoppingcartpricing.adapter.inbound.web.api;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.interview.shoppingcartpricing.adapter.inbound.web.jackson.ProductTypeDeserializer;
import com.interview.shoppingcartpricing.domain.product.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing a single cart item coming from the HTTP layer.
 */
public record CartItemRequest(
        @NotNull
        @JsonDeserialize(using = ProductTypeDeserializer.class)
        ProductType productType,
        @Min(1) int quantity
) {
}
