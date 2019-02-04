package com.raksit.example.order.common.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.raksit.example.order.common.entity.Order;
import com.raksit.example.order.common.util.MockOrderFactory;
import java.util.Optional;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

  @Autowired private OrderRepository orderRepository;

  @After
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  public void createOrder_ShouldSaveOrderIntoDatabase() throws Exception {
    Order order = MockOrderFactory.createSampleOrder();

    orderRepository.save(order);

    Optional<Order> savedOrder = orderRepository.findById(1L);

    assertTrue(savedOrder.isPresent());
    assertEquals(order, savedOrder.get());
  }
}
