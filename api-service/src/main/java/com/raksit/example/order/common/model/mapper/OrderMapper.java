package com.raksit.example.order.common.model.mapper;

import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Money;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.Currency;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Component
public class OrderMapper {

  public Order orderRequestToOrder(OrderRequest orderRequest) {
    return Order.builder()
        .source(orderRequest.getSoldTo())
        .destination(orderRequest.getShipTo())
        .items(orderRequest.getItems().stream()
            .map(this::orderLineItemRequestToOrderLineItem)
            .collect(toList()))
        .build();
  }

  public OrderResponse orderToOrderResponse(Order order) {
    return OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(order.getItems().size())
        .totalPrice(order.getSubTotal())
        .currencies(order.getCurrencies().stream().map(Currency::getCurrencyCode).collect(toList()))
        .build();
  }

  OrderLineItem orderLineItemRequestToOrderLineItem(OrderLineItemRequest lineItemRequest) {
    return OrderLineItem.builder()
        .name(lineItemRequest.getName())
        .money(new Money(lineItemRequest.getPrice(),
            Currency.getInstance(lineItemRequest.getCurrency())))
        .build();
  }
}
