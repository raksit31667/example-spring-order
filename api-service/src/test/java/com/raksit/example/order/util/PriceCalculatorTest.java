package com.raksit.example.order.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriceCalculatorTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  void shouldReturn6000WhenCalculateTotalPriceGiven3OrdersWithPrice2000Each() {
    OrderLineItem orderLineItem = OrderLineItem.builder().name("Diesel").price(2000.0).build();

    List<OrderLineItem> orderLineItems = Collections.nCopies(NUMBER_OF_ITEMS, orderLineItem);

    double expectedTotalPrice = NUMBER_OF_ITEMS * orderLineItem.getPrice();
    double actualTotalPrice = PriceCalculator.calculateTotalPrice(orderLineItems);

    assertEquals(expectedTotalPrice, actualTotalPrice);
  }
}
