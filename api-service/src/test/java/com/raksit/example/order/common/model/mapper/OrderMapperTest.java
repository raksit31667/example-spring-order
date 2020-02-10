package com.raksit.example.order.common.model.mapper;

import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Money;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

  @Spy
  private OrderMapper orderMapper;

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  void shouldReturnOrderWhenOrderRequestToOrderGivenOrderRequestAndCurrencyTHB() {
    // Given
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(1);
    doReturn(OrderLineItem.builder()
        .name(orderRequest.getItems().get(0).getName())
        .money(new Money(orderRequest.getItems().get(0).getPrice(), Currency.getInstance("THB")))
        .build())
        .when(orderMapper)
        .orderLineItemRequestToOrderLineItem(orderRequest.getItems().get(0));

    // When
    Order order = orderMapper.orderRequestToOrder(orderRequest);

    // Then
    assertEquals(orderRequest.getSoldTo(), order.getSource());
    assertEquals(orderRequest.getShipTo(), order.getDestination());
    assertEquals(orderRequest.getItems().get(0).getName(), order.getItems().get(0).getName());
    assertEquals(orderRequest.getItems().get(0).getPrice(), order.getItems().get(0).getMoney().getPrice());
  }

  @Test
  void shouldReturnOrderWhenOrderRequestToOrderGivenOrderRequestAndCurrencyAUD() {
    // Given
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(1);
    doReturn(OrderLineItem.builder()
        .name(orderRequest.getItems().get(0).getName())
        .money(new Money(orderRequest.getItems().get(0).getPrice(), Currency.getInstance("AUD")))
        .build())
        .when(orderMapper)
        .orderLineItemRequestToOrderLineItem(orderRequest.getItems().get(0));

    // When
    Order order = orderMapper.orderRequestToOrder(orderRequest);

    // Then
    assertEquals(orderRequest.getSoldTo(), order.getSource());
    assertEquals(orderRequest.getShipTo(), order.getDestination());
    assertEquals(orderRequest.getItems().get(0).getName(), order.getItems().get(0).getName());
    assertEquals(orderRequest.getItems().get(0).getPrice(), order.getItems().get(0).getMoney().getPrice());
  }

  @Test
  void shouldReturnOrderLineItemWhenOrderLineItemRequestToOrderLineItemGivenOrderLineItemRequest() {
    // Given
    OrderLineItemRequest orderLineItemRequest = MockOrderFactory.createSampleOrderLineItemRequest();

    // When
    OrderLineItem orderLineItem = orderMapper.orderLineItemRequestToOrderLineItem(orderLineItemRequest);

    // Then
    assertEquals(orderLineItem.getName(), orderLineItemRequest.getName());
    assertEquals(orderLineItem.getMoney().getPrice(), orderLineItemRequest.getPrice());
  }

  @Test
  void shouldReturnOrderResponseWhenOrderToOrderResponseGivenOrder() {
    // Given
    Order order = spy(MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS));
    when(order.getCurrency()).thenReturn(Optional.of(Currency.getInstance("USD")));

    // When
    OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);

    // Then
    assertEquals(order.getSource(), orderResponse.getSource());
    assertEquals(order.getDestination(), orderResponse.getDestination());
    assertEquals("USD", orderResponse.getCurrency());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems().intValue());
    assertEquals(3000.0, orderResponse.getTotalPrice().doubleValue());
  }

  @Test
  void shouldReturnOrderResponseWithEmptyCurrencyWhenOrderToOrderResponseGivenOrderItemEmpty() {
    // Given
    Order order = spy(MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS));
    when(order.getCurrency()).thenReturn(Optional.empty());

    // When
    OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);

    // Then
    assertEquals("", orderResponse.getCurrency());
  }
}
