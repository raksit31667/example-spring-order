package com.raksit.example.order.common.repository;

import com.raksit.example.order.common.model.entity.Order;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@AutoConfigureEmbeddedDatabase
class OrderRepositoryTest {

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnListOfOrdersWithSourceNameWhenFindAllBySourceGivenSourceName() {
    Order order = Order.builder()
        .source("Bangkok")
        .build();
    Order anotherOrder = Order.builder()
        .source("Wuhan")
        .build();
    orderRepository.save(order);
    orderRepository.save(anotherOrder);

    List<Order> actualOrders = orderRepository.findAllBySource("Bangkok");

    assertThat(actualOrders, hasSize(1));
    assertThat(actualOrders, containsInAnyOrder(order));
  }
}
