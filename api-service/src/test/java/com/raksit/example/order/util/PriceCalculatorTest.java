package com.raksit.example.order.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PriceCalculatorTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  public void calculateTotalPrice_ShouldReturnCorrectPrice() {
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .price(2000.0)
        .build();

    List<OrderLineItem> orderLineItems = Collections.nCopies(NUMBER_OF_ITEMS, orderLineItem);

    double expectedTotalPrice = NUMBER_OF_ITEMS * orderLineItem.getPrice();
    double actualTotalPrice = PriceCalculator.calculateTotalPrice(orderLineItems);

    assertEquals(expectedTotalPrice, actualTotalPrice);
  }
}
