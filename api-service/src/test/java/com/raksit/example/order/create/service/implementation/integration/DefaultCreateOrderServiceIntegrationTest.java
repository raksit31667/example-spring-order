package com.raksit.example.order.create.service.implementation.integration;

import static org.junit.Assert.assertEquals;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultCreateOrderServiceIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired
  private CreateOrderService createOrderService;

  @Autowired
  private OrderRepository orderRepository;

  @After
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  public void createOrder_ShouldReturnOrderDtoWithNumberOfItemsAndTotalPrice() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    OrderDto orderDto = createOrderService.createOrder(order);

    assertEquals(order.getSource(), orderDto.getSource());
    assertEquals(order.getDestination(), orderDto.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderDto.getNumberOfItems());
    assertEquals(PriceCalculator.calculateTotalPrice(order), orderDto.getTotalPrice(), 0);
  }
}
