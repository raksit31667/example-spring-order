package com.raksit.example.order.create.controller;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.repository.OrderRepository;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class CreateOrderControllerIntegrationTest extends IntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private OrderRepository orderRepository;

  private HttpHeaders httpHeaders;

  @BeforeEach
  void setUp() {
    httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
  }

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest() {
    OrderRequest orderRequest = OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(Collections.nCopies(NUMBER_OF_ITEMS, OrderLineItemRequest.builder()
            .price(1000.0)
            .currency("THB")
            .build()))
        .build();

    HttpEntity<OrderRequest> httpEntity = new HttpEntity<>(orderRequest, httpHeaders);
    ResponseEntity<OrderResponse> responseEntity =
        restTemplate.postForEntity("/orders", httpEntity, OrderResponse.class);
    OrderResponse orderResponse = responseEntity.getBody();

    assertNotNull(orderResponse);
    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    assertEquals("Bangkok", orderResponse.getSource());
    assertEquals("Houston", orderResponse.getDestination());
    assertEquals("THB", orderResponse.getCurrency());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems(), 0);
    assertEquals(3000.0, orderResponse.getTotalPrice(), 0);
  }
}
