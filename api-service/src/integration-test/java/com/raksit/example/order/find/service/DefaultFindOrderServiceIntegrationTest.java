package com.raksit.example.order.find.service;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultFindOrderServiceIntegrationTest extends IntegrationTest {

  @Autowired private FindOrderService findOrderService;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrdersWithSourceBangkokWhenFindOrdersBySourceGivenSourceBangkok() throws Exception {
    Order thaiOrder = Order.builder()
        .source("Bangkok")
        .destination("Houston")
        .items(newArrayList())
        .build();

    Order chineseOrder = Order.builder()
        .source("Wuhan")
        .destination("Houston")
        .items(newArrayList())
        .build();

    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);

    OrderResponse actualOrderResponse =
        findOrderService.findOrdersBySource("Bangkok").iterator().next();

    assertEquals("Bangkok", actualOrderResponse.getSource());
    assertEquals(thaiOrder.getDestination(), actualOrderResponse.getDestination());
  }

  @Test
  void shouldThrowOrderNotFoundExceptionWhenFindOrdersBySourceGivenOrdersWithSourceHoustonNotFound() {
    Order thaiOrder = Order.builder()
        .source("Bangkok")
        .destination("Houston")
        .items(newArrayList())
        .build();

    Order chineseOrder = Order.builder()
        .source("Wuhan")
        .destination("Houston")
        .items(newArrayList())
        .build();

    orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);

    assertThrows(OrderNotFoundException.class, () -> findOrderService.findOrdersBySource("Houston"));
  }
}
