package com.raksit.example.order.common.model.entity;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  void shouldReturn6000WhenGetSubTotalGiven3OrdersWithPrice2000Each() {
    // Given
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .money(new Money(2000.0, Currency.getInstance("THB")))
        .build();
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
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .money(new Money(2250.0, Currency.getInstance("THB")))
        .build();
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

  @Test
  void shouldReturnOptionalCurrencyTHBWhenGetCurrencyGivenAllOrderItemsCurrencyTHB() {
    // Given
    Order order = Order.builder()
        .items(newArrayList(OrderLineItem.builder()
            .money(new Money(1000.0, Currency.getInstance("THB")))
            .money(new Money(2000.0, Currency.getInstance("THB")))
            .money(new Money(3000.0, Currency.getInstance("THB")))
            .build()))
        .build();

    // When
    // Then
    assertEquals(Optional.of(Currency.getInstance("THB")), order.getCurrency());
  }

  @Test
  void shouldReturnOptionalEmptyWhenGetCurrencyGivenNoOrderItems() {
    // Given
    Order order = Order.builder()
        .items(newArrayList())
        .build();

    // When
    // Then
    assertEquals(Optional.empty(), order.getCurrency());
  }
}