package com.interview.shoppingcartpricing.infrastructure.web;

import com.interview.shoppingcartpricing.adapter.inbound.web.WebDtoMapper;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartCalculationRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.CartItemRequest;
import com.interview.shoppingcartpricing.adapter.inbound.web.api.ClientRequest;
import com.interview.shoppingcartpricing.application.CartCalculationCommand;
import com.interview.shoppingcartpricing.application.CartCalculationResult;
import com.interview.shoppingcartpricing.domain.client.IndividualClient;
import com.interview.shoppingcartpricing.domain.client.ProfessionalClient;
import com.interview.shoppingcartpricing.domain.common.Currency;
import com.interview.shoppingcartpricing.domain.product.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class WebDtoMapperTest {

    @Autowired
    WebDtoMapper mapper;

    @Test
    void shouldMapIndividualClientRequestToCommand() {
        ClientRequest client = new ClientRequest(
                "INDIVIDUAL", "C1", "John", "Doe",
                null, null, null, null
        );

        CartCalculationRequest request = new CartCalculationRequest(
                client,
                List.of(new CartItemRequest(ProductType.MID_RANGE_PHONE, 2))
        );

        CartCalculationCommand command = mapper.toCommand(request);

        assertThat(command.client()).isInstanceOf(IndividualClient.class);
        assertThat(command.items()).hasSize(1);
        assertThat(command.items().get(0).quantity()).isEqualTo(2);
    }

    @Test
    void shouldMapProfessionalClientRequestToCommand() {
        ClientRequest client = new ClientRequest(
                "PROFESSIONAL", "P1", null, null,
                "BigCorp", "VAT123", "REG123", BigDecimal.TEN
        );

        CartCalculationRequest request = new CartCalculationRequest(
                client,
                List.of(new CartItemRequest(ProductType.LAPTOP, 1))
        );

        CartCalculationCommand command = mapper.toCommand(request);

        assertThat(command.client()).isInstanceOf(ProfessionalClient.class);
        ProfessionalClient pc = (ProfessionalClient) command.client();
        assertThat(pc.companyName()).isEqualTo("BigCorp");
        assertThat(pc.annualRevenue()).isEqualTo(BigDecimal.TEN);
    }

    @Test
    void shouldMapResultToResponse() {
        CartCalculationResult result = new CartCalculationResult(
                BigDecimal.valueOf(1000),
                Currency.EUR
        );

        var response = mapper.toResponse(result);

        assertThat(response.total()).isEqualTo(BigDecimal.valueOf(1000));
        assertThat(response.currency()).isEqualTo("EUR");
    }
}
