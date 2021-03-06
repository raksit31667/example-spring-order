package com.raksit.example.order.find.service;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultFindOrderServiceIntegrationTest extends IntegrationTest {

  @Autowired private FindOrderService findOrderService;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrdersWithSourceBangkokWhenFindOrdersBySourceGivenSourceBangkok() {
    // Given
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

    Order savedThaiOrder = orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);

    // When
    OrderResponse actual =
        findOrderService.findOrdersBySource("Bangkok").iterator().next();

    // Then
    OrderResponse expected = OrderResponse.builder()
        .id(savedThaiOrder.getId().toString())
        .source("Bangkok")
        .destination("Houston")
        .currencies(newArrayList())
        .numberOfItems(0)
        .totalPrice(0.0)
        .build();
    assertThat(actual, equalTo(expected));
  }

  @Test
  void shouldReturnEmptyOrderWhenFindOrdersBySourceGivenOrdersWithSourceHoustonNotFound() {
    // Given
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

    // When
    List<OrderResponse> actual = findOrderService.findOrdersBySource("XXX");

    // Then
    List<OrderResponse> expected = newArrayList();
    assertThat(actual, equalTo(expected));
  }

  @Test
  void shouldReturnOrderWhenFindOrderByIdGivenOrderWithIdExists() {
    // Given
    Order order = Order.builder()
        .source("Bangkok")
        .destination("Houston")
        .items(newArrayList())
        .build();
    Order savedOrder = orderRepository.save(order);

    // When
    OrderResponse actual = findOrderService.findOrderById(savedOrder.getId());

    // Then
    OrderResponse expected = OrderResponse.builder()
        .id(savedOrder.getId().toString())
        .source("Bangkok")
        .destination("Houston")
        .currencies(newArrayList())
        .numberOfItems(0)
        .totalPrice(0.0)
        .build();
    assertThat(actual, equalTo(expected));
  }

  @Test
  void shouldThrowOrderNotFoundExceptionWhenFindOrderByIdGivenOrderWithIdNotExist() {
    // When
    // Then
    assertThrows(OrderNotFoundException.class, () -> findOrderService.findOrderById(UUID.randomUUID()));
  }
}
