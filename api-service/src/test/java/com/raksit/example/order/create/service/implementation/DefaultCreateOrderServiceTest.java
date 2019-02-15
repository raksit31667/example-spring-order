package com.raksit.example.order.create.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateOrderServiceTest {

  private static final int NUMBER_OF_ITEMS = 3;

  private CreateOrderService createOrderService;

  @Mock private OrderRepository orderRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    createOrderService = new DefaultCreateOrderService(orderRepository);
  }

  @Test
  public void createOrder_ShouldReturnOrderDtoWithNumberOfItemsAndTotalPrice() throws Exception {
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);
    Order order = Order.builder()
        .source(orderRequest.getSoldTo())
        .destination(orderRequest.getShipTo())
        .items(orderRequest.getItems())
        .build();

      when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderResponse orderResponse = createOrderService.createOrder(orderRequest);

    assertEquals(orderRequest.getSoldTo(), orderResponse.getSource());
    assertEquals(orderRequest.getShipTo(), orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems());
    assertEquals(PriceCalculator.calculateTotalPrice(orderRequest.getItems()), orderResponse.getTotalPrice(), 0);
  }
}
