package com.raksit.example.order.common.util;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Collections;

public class MockOrderFactory {

  private static OrderLineItem createSampleOrderLineItems() {
    return OrderLineItem.builder().name("Lube").price(2000.0).build();
  }

  public static Order createSampleOrder() {
    return Order.builder()
        .source("Radiant")
        .destination("Dire")
        .items(Collections.nCopies(3, createSampleOrderLineItems()))
        .build();
  }
}
