package com.interview.shoppingcartpricing.application;

import com.interview.shoppingcartpricing.domain.cart.ShoppingCart;
import com.interview.shoppingcartpricing.domain.calculator.ShoppingCartCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static com.interview.shoppingcartpricing.domain.common.Currency.EUR;

/**
 * Application service implementing the cart total calculation use case.
 * <p>
 * This service orchestrates the domain model and exposes an intention-revealing API.
 */
@Slf4j
@Service
public class CartCalculationService implements CartCalculationUseCase {

    private final ShoppingCartCalculator calculator;

    public CartCalculationService() {
        // Domain service is stateless, we can safely instantiate it directly.
        this.calculator = new ShoppingCartCalculator();
    }

    @Override
    public CartCalculationResult calculate(CartCalculationCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        log.debug("Starting cart total calculation for client {}", command.client().id());

        var cart = new ShoppingCart(command.client(), command.items());
        BigDecimal total = calculator.calculateTotal(cart);

        log.debug("Calculated total {} {}", total, EUR.name());

        return new CartCalculationResult(total, EUR);
    }
}
