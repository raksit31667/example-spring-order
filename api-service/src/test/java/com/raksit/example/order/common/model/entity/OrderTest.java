package com.raksit.example.order.common.model.entity;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  void shouldReturn6000WhenGetSubTotalGiven3OrdersWithPrice2000Each() {
    // Given
    OrderLineItem orderLineItem = OrderLineItem.builder().price(2000.0).build();
    List<OrderLineItem> orderLineItems = Collections.nCopies(NUMBER_OF_ITEMS, orderLineItem);
    Order order = Order.builder()
        .items(orderLineItems)
        .build();

    // When
    double actualTotalPrice = order.getSubTotal();

    // Then
    assertEquals(6000.0, actualTotalPrice);
  }

  @Test
  void shouldReturn4500WhenGetSubTotalGiven2OrdersWithPrice2250Each() {
    // Given
    OrderLineItem orderLineItem = OrderLineItem.builder().name("Diesel").price(2250.0).build();
    List<OrderLineItem> orderLineItems = Collections.nCopies(2, orderLineItem);
    Order order = Order.builder()
        .items(orderLineItems)
        .build();

    // When
    double actualTotalPrice = order.getSubTotal();

    // Then
    assertEquals(4500.0, actualTotalPrice);
  }

  @Test
  void shouldReturn0WhenGetSubTotalGivenNoOrders() {
    // Given
    Order order = Order.builder()
        .items(newArrayList())
        .build();

    // When
    double actualTotalPrice = order.getSubTotal();

    // Then
    assertEquals(0, actualTotalPrice);
  }
}