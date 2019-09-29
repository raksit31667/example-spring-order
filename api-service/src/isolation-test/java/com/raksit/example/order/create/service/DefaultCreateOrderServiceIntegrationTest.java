package com.raksit.example.order.create.service;

import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.repository.OrderRepository;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureEmbeddedDatabase
@SpringBootTest
class DefaultCreateOrderServiceIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private CreateOrderService createOrderService;

  @Autowired private OrderRepository orderRepository;

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
            .build()))
        .build();

    OrderResponse orderResponse = createOrderService.createOrder(orderRequest);

    assertEquals("Bangkok", orderResponse.getSource());
    assertEquals("Houston", orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems());
    assertEquals(3000.0, orderResponse.getTotalPrice(), 0);
  }
}
