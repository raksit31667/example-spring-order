package com.raksit.example.order.find.service.implementation.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.find.service.FindOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureEmbeddedDatabase
@SpringBootTest
class DefaultFindOrderServiceIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private FindOrderService findOrderService;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrdersWithSourceBangkokWhenFindOrdersBySourceGivenSourceBangkok() throws Exception {
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);
    OrderResponse actualOrderResponse =
        findOrderService.findOrdersBySource("Bangkok").iterator().next();

    assertEquals("Bangkok", actualOrderResponse.getSource());
    assertEquals(thaiOrder.getDestination(), actualOrderResponse.getDestination());
  }

  @Test
  void shouldThrowOrderNotFoundExceptionWhenFindOrdersBySourceGivenOrdersWithSourceHoustonNotFound() {
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);

    assertThrows(OrderNotFoundException.class, () -> findOrderService.findOrdersBySource("Houston"));
  }
}
