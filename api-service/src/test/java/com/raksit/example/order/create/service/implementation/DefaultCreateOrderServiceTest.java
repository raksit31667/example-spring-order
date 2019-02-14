package com.raksit.example.order.create.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.raksit.example.order.common.model.dto.OrderDto;
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
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
      when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderDto orderDto = createOrderService.createOrder(order);

    assertEquals(order.getSource(), orderDto.getSource());
    assertEquals(order.getDestination(), orderDto.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderDto.getNumberOfItems());
    assertEquals(PriceCalculator.calculateTotalPrice(order), orderDto.getTotalPrice(), 0);
  }
}
