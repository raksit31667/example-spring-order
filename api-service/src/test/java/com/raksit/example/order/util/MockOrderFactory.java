package com.raksit.example.order.util;

import com.github.javafaker.Faker;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Collections;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MockOrderFactory {

  private static final Faker FAKER = new Faker();

  public static OrderLineItem createSampleOrderLineItems() {
    return OrderLineItem.builder()
        .name(FAKER.beer().name())
        .price(FAKER.number().randomDouble(2, 1000, 2000))
        .build();
  }

  public static OrderLineItemRequest createSampleOrderLineItemRequests() {
    return OrderLineItemRequest.builder()
        .name(FAKER.beer().name())
        .price(FAKER.number().randomDouble(2, 1000, 2000))
        .build();
  }

  public static OrderRequest createSampleOrderRequest(int numberOfItems) {
    return OrderRequest.builder()
        .soldTo(FAKER.company().name())
        .shipTo(FAKER.company().name())
        .items(Collections.nCopies(numberOfItems, createSampleOrderLineItems()))
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
