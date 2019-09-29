package com.raksit.example.order.common.model.mapper;

import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.util.PriceCalculator;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

  public Order orderRequestToOrder(OrderRequest orderRequest) {
    return Order.builder()
        .source(orderRequest.getSoldTo())
        .destination(orderRequest.getShipTo())
        .items(orderRequest.getItems())
        .build();
  }

  public OrderLineItem orderLineItemRequestToOrderLineItem(OrderLineItemRequest lineItemRequest) {
    return OrderLineItem.builder()
        .name(lineItemRequest.getName())
        .price(lineItemRequest.getPrice())
        .build();
  }

  public OrderResponse orderToOrderResponse(Order order) {
    return OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(order.getItems().size())
        .totalPrice(PriceCalculator.calculateTotalPrice(order.getItems()))
        .build();
  }
}
