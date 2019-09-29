package com.raksit.example.order.create.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultCreateOrderServiceTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Mock private OrderRepository orderRepository;

  @Mock
  private OrderMapper orderMapper;

  @InjectMocks private DefaultCreateOrderService createOrderService;

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest() {
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);
    Order order = Order.builder()
            .source(orderRequest.getSoldTo())
            .destination(orderRequest.getShipTo())
            .items(orderRequest.getItems())
            .build();

    when(orderMapper.orderRequestToOrder(orderRequest)).thenReturn(order);
    when(orderRepository.save(order)).thenReturn(order);
    when(orderMapper.orderToOrderResponse(order)).thenReturn(OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(NUMBER_OF_ITEMS)
        .totalPrice(PriceCalculator.calculateTotalPrice(orderRequest.getItems()))
        .build());

    OrderResponse orderResponse = createOrderService.createOrder(orderRequest);

    assertEquals(orderRequest.getSoldTo(), orderResponse.getSource());
    assertEquals(orderRequest.getShipTo(), orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems());
    assertEquals(
        PriceCalculator.calculateTotalPrice(orderRequest.getItems()),
        orderResponse.getTotalPrice(),
        0);
  }
}
