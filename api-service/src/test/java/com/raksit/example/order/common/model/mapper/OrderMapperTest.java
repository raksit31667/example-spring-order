package com.raksit.example.order.common.model.mapper;

import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.util.MockOrderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

  @Spy
  private OrderMapper orderMapper;

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  void shouldReturnOrderWhenOrderRequestToOrderGivenOrderRequest() {
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(1);
    doReturn(OrderLineItem.builder()
        .name(orderRequest.getItems().get(0).getName())
        .price(orderRequest.getItems().get(0).getPrice())
        .build())
        .when(orderMapper)
        .orderLineItemRequestToOrderLineItem(orderRequest.getItems().get(0));

    Order order = orderMapper.orderRequestToOrder(orderRequest);

    assertEquals(orderRequest.getSoldTo(), order.getSource());
    assertEquals(orderRequest.getShipTo(), order.getDestination());

    assertEquals(orderRequest.getItems().get(0).getName(), order.getItems().get(0).getName());
    assertEquals(orderRequest.getItems().get(0).getPrice(), order.getItems().get(0).getPrice(), 0);
  }

  @Test
  void shouldReturnOrderLineItemWhenOrderLineItemRequestToOrderLineItemGivenOrderLineItemRequest() {
    OrderLineItemRequest orderLineItemRequest = MockOrderFactory.createSampleOrderLineItemRequest();

    OrderLineItem orderLineItem = orderMapper.orderLineItemRequestToOrderLineItem(orderLineItemRequest);

    assertEquals(orderLineItem.getName(), orderLineItemRequest.getName());
    assertEquals(orderLineItem.getPrice(), orderLineItemRequest.getPrice());
  }

  @Test
  void shouldReturnOrderResponseWhenOrderToOrderResponseGivenOrder() {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);

    assertEquals(order.getSource(), orderResponse.getSource());
    assertEquals(order.getDestination(), orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems());
    assertEquals(3000.0, orderResponse.getTotalPrice(), 0);
  }
}
