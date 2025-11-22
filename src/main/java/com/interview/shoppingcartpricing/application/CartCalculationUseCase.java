package com.interview.shoppingcartpricing.application;

/**
 * Use case boundary for calculating the total of a shopping cart.
 */
public interface CartCalculationUseCase {

    CartCalculationResult calculate(CartCalculationCommand command);
}
