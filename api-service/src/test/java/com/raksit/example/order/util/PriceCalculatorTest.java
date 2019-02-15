package com.raksit.example.order.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class PriceCalculatorTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  public void calculateTotalPrice_ShouldReturnCorrectPrice() {
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .price(2000.0)
        .build();

    OrderRequest orderRequest = OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(Collections.nCopies(NUMBER_OF_ITEMS, orderLineItem))
        .build();

    double expectedTotalPrice = NUMBER_OF_ITEMS * orderLineItem.getPrice();
    double actualTotalPrice = PriceCalculator.calculateTotalPrice(orderRequest);

    assertEquals(expectedTotalPrice, actualTotalPrice);
  }
}
