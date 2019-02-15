package com.raksit.example.order.util;

import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.List;

public class PriceCalculator {

  public static double calculateTotalPrice(List<OrderLineItem> orderLineItems) {
    return orderLineItems.stream().mapToDouble(OrderLineItem::getPrice).sum();
  }
}
