package com.raksit.example.order.create.service.implementation.integration;

import static org.junit.Assert.assertEquals;

import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DefaultCreateOrderServiceIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired
  private CreateOrderService createOrderService;

  @Autowired
  private OrderRepository orderRepository;

  @AfterEach
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  public void createOrder_ShouldReturnOrderDtoWithNumberOfItemsAndTotalPrice() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    OrderResponse orderResponse = createOrderService.createOrder(order);

    assertEquals(order.getSource(), orderResponse.getSource());
    assertEquals(order.getDestination(), orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems());
    assertEquals(PriceCalculator.calculateTotalPrice(order), orderResponse.getTotalPrice(), 0);
  }
}