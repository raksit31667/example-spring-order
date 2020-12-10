package com.raksit.example.order.common.repository;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.find.specification.OrderSpecificationBuilder;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;

import java.util.Collections;
import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
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

  private final OrderSpecificationBuilder orderSpecificationBuilder = new OrderSpecificationBuilder();

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

  @Test
  void shouldReturnAllOfOrdersWithSourceOrDestinationWhenFindAllBySpecificationGivenMatchedKeyword() {
    // Given
    Order thaiOrder = Order.builder()
        .source("Bangkok")
        .build();
    Order chineseOrder = Order.builder()
        .destination("Wuhan")
        .build();
    Order usOrder = Order.builder()
        .source("Portland")
        .source("Atlanta")
        .build();
    orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);
    orderRepository.save(usOrder);

    // When
    Specification<Order> orderSpecification = orderSpecificationBuilder.buildCriteria("an");
    List<Order> actualOrders = orderRepository.findAll(orderSpecification);

    // Then
    assertThat(actualOrders, hasSize(3));
    assertThat(actualOrders, containsInAnyOrder(thaiOrder, chineseOrder, usOrder));
  }

  @Test
  void shouldReturnSomeOfOrdersWithSourceOrDestinationWhenFindAllBySpecificationGivenPartiallyMatchedKeyword() {
    // Given
    Order thaiOrder = Order.builder()
        .source("Bangkok")
        .build();
    Order usOrder = Order.builder()
        .source("Dallas")
        .destination("Houston")
        .build();
    orderRepository.save(thaiOrder);
    orderRepository.save(usOrder);

    // When
    Specification<Order> orderSpecification = orderSpecificationBuilder.buildCriteria(" LL ");
    List<Order> actualOrders = orderRepository.findAll(orderSpecification);

    // Then
    assertThat(actualOrders, hasSize(1));
    assertThat(actualOrders, containsInAnyOrder(usOrder));
  }

  @Test
  void shouldReturnEmptyWithSourceOrDestinationWhenFindAllBySpecificationGivenUnmatchedKeyword() {
    // Given
    Order thaiOrder = Order.builder()
        .source("Bangkok")
        .build();
    Order usOrder = Order.builder()
        .source("Dallas")
        .destination("Houston")
        .build();
    orderRepository.save(thaiOrder);
    orderRepository.save(usOrder);

    // When
    Specification<Order> orderSpecification = orderSpecificationBuilder.buildCriteria("sa");
    List<Order> actualOrders = orderRepository.findAll(orderSpecification);

    // Then
    assertThat(actualOrders, IsEmptyCollection.empty());
  }
}
