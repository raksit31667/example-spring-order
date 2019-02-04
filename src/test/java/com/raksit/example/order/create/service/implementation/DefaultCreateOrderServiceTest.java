package com.raksit.example.order.create.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.common.util.MockOrderFactory;
import com.raksit.example.order.create.service.CreateOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCreateOrderServiceTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @InjectMocks
  private CreateOrderService createOrderService = new DefaultCreateOrderService();

  @Mock
  private OrderRepository orderRepository;

  @Test
  public void createOrder() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderDto orderDto = createOrderService.createOrder(order);

    assertEquals(order.getSource(), orderDto.getSource());
    assertEquals(order.getDestination(), orderDto.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderDto.getNumberOfItems());
    assertEquals(order.getTotalPrice(), orderDto.getTotalPrice(), 0);
  }
}
