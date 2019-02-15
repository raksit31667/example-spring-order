package com.raksit.example.order.common.model.mapper;

import static org.junit.Assert.assertEquals;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  public void orderRequestToOrder() {
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);

    Order order = OrderMapper.INSTANCE.orderRequestToOrder(orderRequest);

    assertEquals(orderRequest.getSoldTo(), order.getSource());
    assertEquals(orderRequest.getShipTo(), order.getDestination());
    assertEquals(orderRequest.getItems(), order.getItems());
  }

  @Test
  public void orderToOrderResponse() {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    OrderResponse orderResponse = OrderMapper.INSTANCE.orderToOrderResponse(order);

    assertEquals(order.getSource(), orderResponse.getSource());
    assertEquals(order.getDestination(), orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems());
    assertEquals(PriceCalculator.calculateTotalPrice(order.getItems()), orderResponse.getTotalPrice(), 0);
  }
}
