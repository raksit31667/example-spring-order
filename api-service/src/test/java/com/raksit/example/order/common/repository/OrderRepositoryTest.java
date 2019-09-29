package com.raksit.example.order.common.repository;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.util.MockOrderFactory;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureEmbeddedDatabase
class OrderRepositoryTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnListOfOrdersWithSourceNameWhenFindAllBySourceGivenSourceName() {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order anotherOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    orderRepository.save(order);
    orderRepository.save(anotherOrder);

    Optional<List<Order>> actualOrders = orderRepository.findAllBySource(order.getSource());

    assertTrue(actualOrders.isPresent());
    assertEquals(1, actualOrders.get().size());
    assertEquals(order, actualOrders.get().iterator().next());
  }
}
