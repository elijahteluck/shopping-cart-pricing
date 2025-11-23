package com.interview.shoppingcartpricing.adapter.inbound.web;


import com.interview.shoppingcartpricing.application.CartCalculationUseCase;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing an API to calculate shopping cart totals.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {

    private final CartCalculationUseCase useCase;
    private final WebDtoMapper mapper;

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
