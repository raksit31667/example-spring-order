package com.raksit.example.order.find.controller;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.find.service.FindOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindOrderControllerTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Mock
  private FindOrderService findOrderService;

  @InjectMocks
  private FindOrderController findOrderController;

  @Test
  void shouldReturnOrdersWithBangkokSourceWhenFindOrdersBySourceGivenSourceBangkok() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    order.setSource("Bangkok");

    OrderResponse orderResponse = OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .totalPrice(3000.0)
        .build();

    when(findOrderService.findOrdersBySource(eq("Bangkok")))
        .thenReturn(Collections.singletonList(orderResponse));

    List<OrderResponse> actual = findOrderController.findOrdersBySource("Bangkok");

    List<OrderResponse> expected = newArrayList(orderResponse);
    assertEquals(expected, actual);
  }

  @Test
  void shouldReturnStatusNotFoundWhenFindOrdersBySourceGivenOrdersWithSourceBangkokNotFound() throws Exception {
    when(findOrderService.findOrdersBySource(eq("Bangkok"))).thenThrow(new OrderNotFoundException());

    assertThrows(OrderNotFoundException.class, () -> findOrderController.findOrdersBySource("Bangkok"));
  }
}
