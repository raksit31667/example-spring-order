package com.raksit.example.order.util;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.entity.OrderLineItem;

public class PriceCalculator {

  public static double calculateTotalPrice(OrderRequest orderRequest) {
    return orderRequest.getItems().stream().mapToDouble(OrderLineItem::getPrice).sum();
  }
}
