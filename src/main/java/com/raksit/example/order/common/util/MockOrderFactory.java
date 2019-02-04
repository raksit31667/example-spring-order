package com.raksit.example.order.common.util;

import com.github.javafaker.Faker;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Collections;

public class MockOrderFactory {

  private static final Faker FAKER = new Faker();

  private static OrderLineItem createSampleOrderLineItems() {
    return OrderLineItem.builder()
        .name(FAKER.beer().name())
        .price(FAKER.number().randomDouble(2, 1000, 2000))
        .build();
  }

  public static Order createSampleOrder(int numberOfItems) {
    return Order.builder()
        .source(FAKER.company().name())
        .destination(FAKER.company().name())
        .items(Collections.nCopies(numberOfItems, createSampleOrderLineItems()))
        .build();
  }
}
