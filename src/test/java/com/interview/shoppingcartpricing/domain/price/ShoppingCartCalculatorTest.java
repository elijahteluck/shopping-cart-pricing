package com.interview.shoppingcartpricing.domain.price;

import com.interview.shoppingcartpricing.domain.calculator.ShoppingCartCalculator;
import com.interview.shoppingcartpricing.domain.cart.CartItem;
import com.interview.shoppingcartpricing.domain.cart.ShoppingCart;
import com.interview.shoppingcartpricing.domain.client.IndividualClient;
import com.interview.shoppingcartpricing.domain.client.ProfessionalClient;
import com.interview.shoppingcartpricing.domain.product.ProductType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ShoppingCartCalculator}.
 *
 * These tests verify that the calculator correctly applies pricing rules
 * based on the type of client (individual, professional with high revenue,
 * professional with low revenue) and produces the expected total amounts
 * for different shopping cart configurations.
 */
class ShoppingCartCalculatorTest {

    private final ShoppingCartCalculator calculator = new ShoppingCartCalculator();

    @Test
    void shouldCalculateTotalForIndividualClient() {
        var client = new IndividualClient("C1", "John", "Doe");
        var items = List.of(
                new CartItem(ProductType.HIGH_END_PHONE, 1),
                new CartItem(ProductType.MID_RANGE_PHONE, 2),
                new CartItem(ProductType.LAPTOP, 1)
        );
        var cart = new ShoppingCart(client, items);

        BigDecimal total = calculator.calculateTotal(cart);

        assertThat(total).isEqualByComparingTo("4300");
    }

    @Test
    void shouldApplyHighRevenueProfessionalPrices() {
        var client = new ProfessionalClient(
                "P1",
                "Big Corp",
                "VAT123",
                "REG123",
                BigDecimal.valueOf(20_000_000L)
        );

        var items = List.of(
                new CartItem(ProductType.HIGH_END_PHONE, 2),
                new CartItem(ProductType.LAPTOP, 1)
        );
        var cart = new ShoppingCart(client, items);

        BigDecimal total = calculator.calculateTotal(cart);

        assertThat(total).isEqualByComparingTo("2900");
    }

    @Test
    void shouldApplyLowRevenueProfessionalPrices() {
        var client = new ProfessionalClient(
                "P2",
                "Small Biz",
                "VAT456",
                "REG456",
                BigDecimal.valueOf(5_000_000L) // < 10M
        );

        var items = List.of(
                new CartItem(ProductType.MID_RANGE_PHONE, 3),
                new CartItem(ProductType.LAPTOP, 2)
        );
        var cart = new ShoppingCart(client, items);

        BigDecimal total = calculator.calculateTotal(cart);

        assertThat(total).isEqualByComparingTo("3800");
    }
}
