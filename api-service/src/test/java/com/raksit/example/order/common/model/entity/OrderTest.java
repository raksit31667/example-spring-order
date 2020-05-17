package com.raksit.example.order.common.model.entity;

import java.util.Collections;
import java.util.Currency;
import java.util.List;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

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
    assertThat(actualTotalPrice, equalTo(6000.0));
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
    assertThat(actualTotalPrice, equalTo(4500.0));
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
    assertThat(actualTotalPrice, equalTo(0.0));
  }

  @Test
  void shouldReturnCurrencyListTHBWhenGetCurrencyGivenAllOrderItemsCurrencyTHB() {
    // Given
    Order order = Order.builder()
        .items(newArrayList(OrderLineItem.builder()
            .money(new Money(1000.0, Currency.getInstance("THB")))
            .build()))
        .build();

    // When
    // Then
    assertThat(order.getCurrencies(), containsInAnyOrder(Currency.getInstance("THB")));
  }

  @Test
  void shouldReturnCurrencyListTHBAndUSDWhenGetCurrencyGivenOrderItemsCurrencyTHBAndUSD() {
    // Given
    Order order = Order.builder()
        .items(newArrayList(
            OrderLineItem.builder()
            .money(new Money(1000.0, Currency.getInstance("USD")))
            .build(),
            OrderLineItem.builder()
                .money(new Money(3000.0, Currency.getInstance("THB")))
                .build()))
        .build();

    // When
    // Then
    assertThat(order.getCurrencies(), containsInAnyOrder(Currency.getInstance("USD"), Currency.getInstance("THB")));
  }

  @Test
  void shouldReturnCurrencyListTHBWhenGetCurrencyGivenOrderAllItemsCurrencyTHB() {
    // Given
    Order order = Order.builder()
        .items(newArrayList(
            OrderLineItem.builder()
                .money(new Money(1000.0, Currency.getInstance("THB")))
                .build(),
            OrderLineItem.builder()
                .money(new Money(2000.0, Currency.getInstance("THB")))
                .build(),
            OrderLineItem.builder()
                .money(new Money(3000.0, Currency.getInstance("THB")))
                .build(),
            OrderLineItem.builder()
                .money(new Money(4000.0, Currency.getInstance("THB")))
                .build()))
        .build();

    // When
    // Then
    assertThat(order.getCurrencies(), containsInAnyOrder(Currency.getInstance("THB")));
  }

  @Test
  void shouldReturnCurrencyListTHBWhenGetCurrencyGivenOrderAllItemsCurrencyAUDAndJPY() {
    // Given
    Order order = Order.builder()
        .items(newArrayList(
            OrderLineItem.builder()
                .money(new Money(1000.0, Currency.getInstance("AUD")))
                .build(),
            OrderLineItem.builder()
                .money(new Money(2000.0, Currency.getInstance("JPY")))
                .build(),
            OrderLineItem.builder()
                .money(new Money(3000.0, Currency.getInstance("JPY")))
                .build(),
            OrderLineItem.builder()
                .money(new Money(4000.0, Currency.getInstance("AUD")))
                .build()))
        .build();

    // When
    // Then
    assertThat(order.getCurrencies(), containsInAnyOrder(Currency.getInstance("AUD"), Currency.getInstance("JPY")));
  }

  @Test
  void shouldReturnEmptyWhenGetCurrencyGivenNoOrderItems() {
    // Given
    Order order = Order.builder()
        .items(newArrayList())
        .build();

    // When
    // Then
    assertThat(order.getCurrencies(), IsEmptyCollection.empty());
  }
}