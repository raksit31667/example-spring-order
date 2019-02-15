package com.raksit.example.order.common.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class OrderRepositoryTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  public void createOrder_ShouldSaveOrderIntoDatabase() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    orderRepository.save(order);

    Optional<Order> savedOrder = orderRepository.findById(1L);

    assertTrue(savedOrder.isPresent());
    assertEquals(order, savedOrder.get());
  }
}