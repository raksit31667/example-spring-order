package com.raksit.example.order.util;

import com.github.javafaker.Faker;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.entity.Money;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Collections;
import java.util.Currency;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MockOrderFactory {

  private static final Faker FAKER = new Faker();

  public static OrderLineItem createSampleOrderLineItems() {
    return OrderLineItem.builder()
        .name(FAKER.beer().name())
        .money(new Money(1000.0, Currency.getInstance("THB")))
        .build();
  }

  public static OrderLineItemRequest createSampleOrderLineItemRequest() {
    return OrderLineItemRequest.builder()
        .name(FAKER.beer().name())
        .price(1000.0)
        .currency("THB")
        .build();
  }

  public static OrderRequest createSampleOrderRequest(int numberOfItems) {
    return OrderRequest.builder()
        .soldTo(FAKER.company().name())
        .shipTo(FAKER.company().name())
        .items(Collections.nCopies(numberOfItems, createSampleOrderLineItemRequest()))
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
