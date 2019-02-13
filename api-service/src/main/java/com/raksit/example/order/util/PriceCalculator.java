package com.raksit.example.order.util;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;

public class PriceCalculator {

  public static double calculateTotalPrice(Order order) {
    return order.getItems().stream().mapToDouble(OrderLineItem::getPrice).sum();
  }
}
