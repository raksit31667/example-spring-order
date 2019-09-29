package com.raksit.example.order.common.model.mapper;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  void shouldReturnOrderWhenOrderRequestToOrderGivenOrderRequest() {
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);

    Order order = OrderMapper.INSTANCE.orderRequestToOrder(orderRequest);

    assertEquals(orderRequest.getSoldTo(), order.getSource());
    assertEquals(orderRequest.getShipTo(), order.getDestination());
    assertEquals(orderRequest.getItems(), order.getItems());
  }

  @Test
  void shouldReturnOrderLineItemWhenOrderLineItemRequestToOrderLineItemGivenOrderLineItemRequest() {
    OrderLineItemRequest orderLineItemRequest = MockOrderFactory.createSampleOrderLineItemRequests();

    OrderLineItem orderLineItem = OrderMapper.INSTANCE.orderLineItemRequestToOrderLineItem(orderLineItemRequest);

    assertEquals(orderLineItem.getName(), orderLineItemRequest.getName());
    assertEquals(orderLineItem.getPrice(), orderLineItemRequest.getPrice());
  }

  @Test
  void shouldReturnOrderResponseWhenOrderToOrderResponseGivenOrder() {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    OrderResponse orderResponse = OrderMapper.INSTANCE.orderToOrderResponse(order);

    assertEquals(order.getSource(), orderResponse.getSource());
    assertEquals(order.getDestination(), orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems());
    assertEquals(
        PriceCalculator.calculateTotalPrice(order.getItems()), orderResponse.getTotalPrice(), 0);
  }
}
