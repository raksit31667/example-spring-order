package com.raksit.example.order.common.model.mapper;

import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Money;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Currency;
import java.util.UUID;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
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
    assertThat(order.getSource(), equalTo(orderRequest.getSoldTo()));
    assertThat(order.getDestination(), equalTo(orderRequest.getShipTo()));
    assertThat(order.getItems().get(0).getName(),
        equalTo(orderRequest.getItems().get(0).getName()));
    assertThat(order.getItems().get(0).getMoney().getPrice(),
        equalTo(orderRequest.getItems().get(0).getPrice()));
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
    assertThat(order.getSource(), equalTo(orderRequest.getSoldTo()));
    assertThat(order.getDestination(), equalTo(orderRequest.getShipTo()));
    assertThat(order.getItems().get(0).getName(),
        equalTo(orderRequest.getItems().get(0).getName()));
    assertThat(order.getItems().get(0).getMoney().getPrice(),
        equalTo(orderRequest.getItems().get(0).getPrice()));
  }

  @Test
  void shouldReturnOrderLineItemWhenOrderLineItemRequestToOrderLineItemGivenOrderLineItemRequest() {
    // Given
    OrderLineItemRequest orderLineItemRequest = MockOrderFactory.createSampleOrderLineItemRequest();

    // When
    OrderLineItem orderLineItem = orderMapper.orderLineItemRequestToOrderLineItem(orderLineItemRequest);

    // Then
    assertThat(orderLineItem.getName(), equalTo(orderLineItemRequest.getName()));
    assertThat(orderLineItem.getMoney().getPrice(), equalTo(orderLineItemRequest.getPrice()));
  }

  @Test
  void shouldReturnOrderResponseWhenOrderToOrderResponseGivenOrder() {
    // Given
    UUID uuid = UUID.randomUUID();
    Order order = spy(MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS));
    when(order.getId()).thenReturn(uuid);
    when(order.getCurrencies()).thenReturn(newArrayList(Currency.getInstance("USD")));

    // When
    OrderResponse actual = orderMapper.orderToOrderResponse(order);

    // Then
    OrderResponse expected = OrderResponse.builder()
        .id(uuid.toString())
        .source(order.getSource())
        .destination(order.getDestination())
        .currencies(newArrayList("USD"))
        .numberOfItems(NUMBER_OF_ITEMS)
        .totalPrice(3000.0)
        .build();
    assertThat(actual, equalTo(expected));
  }

  @Test
  void shouldReturnOrderResponseWithEmptyCurrencyWhenOrderToOrderResponseGivenOrderItemEmpty() {
    // Given
    Order order = spy(MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS));
    when(order.getId()).thenReturn(UUID.randomUUID());
    when(order.getCurrencies()).thenReturn(newArrayList());

    // When
    OrderResponse orderResponse = orderMapper.orderToOrderResponse(order);

    // Then
    assertThat(orderResponse.getCurrencies(), IsEmptyCollection.empty());
  }
}
