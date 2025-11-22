package com.interview.shoppingcartpricing.adapter.inbound.web;


import com.interview.shoppingcartpricing.application.CartCalculationUseCase;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing an API to calculate shopping cart totals.
 */
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartCalculationUseCase useCase;
    private final WebDtoMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    public CartController(CartCalculationUseCase useCase, WebDtoMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    /**
     * Calculates the total of a shopping cart for a given client.
     *
     * @param request incoming request with client information and cart items
     * @return total amount including applied pricing rules
     */
    @PostMapping("/total")
    public ResponseEntity<CartCalculationResponse> calculateTotal(@Valid @RequestBody CartCalculationRequest request) {
        log.debug("Incoming request: {}", request);

        var command = mapper.toCommand(request);
        var result = useCase.calculate(command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }
}
