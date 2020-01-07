package com.raksit.example.order.create.controller;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.create.service.CreateOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderControllerTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Mock
  private CreateOrderService createOrderService;

  @InjectMocks
  private CreateOrderController createOrderController;

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest() {
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);
    OrderResponse orderResponse = OrderResponse.builder()
            .source(orderRequest.getSoldTo())
            .destination(orderRequest.getShipTo())
            .numberOfItems(NUMBER_OF_ITEMS)
            .totalPrice(3000.0)
            .build();

    when(createOrderService.createOrder(orderRequest)).thenReturn(orderResponse);

    OrderResponse actual = createOrderController.createOrder(orderRequest);

    assertEquals(orderResponse, actual);
  }
}
