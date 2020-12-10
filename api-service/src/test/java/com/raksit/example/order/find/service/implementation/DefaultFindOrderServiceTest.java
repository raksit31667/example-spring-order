package com.raksit.example.order.find.service.implementation;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.find.specification.OrderSpecificationBuilder;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultFindOrderServiceTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Mock private OrderRepository orderRepository;

  @Mock private OrderSpecificationBuilder orderSpecificationBuilder;

  @Mock private Specification<Order> orderSpecification;

  @Mock
  private OrderMapper orderMapper;

  @InjectMocks private DefaultFindOrderService findOrderService;

  @Test
  void shouldReturnOrdersWithSourceBangkokWhenFindOrdersBySourceGivenSourceBangkok() {
    // Given
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    when(orderRepository.findAllBySource(eq("Bangkok")))
        .thenReturn(Collections.singletonList(thaiOrder));
    when(orderMapper.orderToOrderResponse(thaiOrder)).thenReturn(OrderResponse.builder()
        .source("Bangkok")
        .destination(thaiOrder.getDestination())
        .build());

    // When
    OrderResponse actualOrderResponse =
        findOrderService.findOrdersBySource("Bangkok").iterator().next();

    // Then
    assertThat(actualOrderResponse.getSource(), equalTo("Bangkok"));
  }

  @Test
  void shouldReturnEmptyOrderWhenFindOrdersBySourceGivenOrdersWithSourceHoustonNotFound() {
    // Given
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");
    when(orderRepository.findAllBySource(eq("Houston"))).thenReturn(newArrayList());

    // When
    List<OrderResponse> actual = findOrderService.findOrdersBySource("Houston");

    // Then
    assertThat(actual, IsEmptyCollection.empty());
  }

  @Test
  void shouldReturnOrdersWithSourceOrDestinationBangkokWhenFindOrdersByKeywordGivenKeywordKok() {
    // Given
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("BANGKOK");
    chineseOrder.setDestination("bangkok");

    when(orderSpecificationBuilder.buildCriteria("Kok")).thenReturn(orderSpecification);
    when(orderRepository.findAll(orderSpecification)).thenReturn(newArrayList(thaiOrder, chineseOrder));
    doReturn(OrderResponse.builder()
        .source(thaiOrder.getSource())
        .destination(thaiOrder.getDestination())
        .build()).when(orderMapper).orderToOrderResponse(thaiOrder);
    doReturn(OrderResponse.builder()
        .source(chineseOrder.getSource())
        .destination(chineseOrder.getDestination())
        .build()).when(orderMapper).orderToOrderResponse(chineseOrder);

    // When
    Iterator<OrderResponse> orderResponseIterator = findOrderService.findOrdersByKeyword("Kok").iterator();
    OrderResponse thaiOrderResponse = orderResponseIterator.next();
    OrderResponse chineseOrderResponse = orderResponseIterator.next();

    // Then
    assertThat(thaiOrderResponse.getSource(), equalTo("BANGKOK"));
    assertThat(chineseOrderResponse.getDestination(), equalTo("bangkok"));
  }

  @Test
  void shouldReturnEmptyOrderWhenFindOrdersByKeywordGivenNoOrdersMatchKeywordAbc() {
    // Given
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    when(orderSpecificationBuilder.buildCriteria("abC")).thenReturn(orderSpecification);
    when(orderRepository.findAll(orderSpecification)).thenReturn(newArrayList());

    // When
    List<OrderResponse> actual = findOrderService.findOrdersByKeyword("abC");

    // Then
    assertThat(actual, IsEmptyCollection.empty());
  }

  @Test
  void shouldReturnOrderResponseWhenFindOrderByIdGivenOrderIdExists() throws OrderNotFoundException {
    // Given
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    OrderResponse orderResponse = OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(NUMBER_OF_ITEMS)
        .totalPrice(6000.0)
        .build();
    UUID uuid = UUID.randomUUID();
    when(orderRepository.findById(uuid)).thenReturn(Optional.of(order));
    when(orderMapper.orderToOrderResponse(order)).thenReturn(orderResponse);

    // When
    OrderResponse actual = findOrderService.findOrderById(uuid);

    // Then
    assertThat(actual, equalTo(orderResponse));
  }

  @Test
  void shouldThrowOrderNotFoundExceptionWhenFindOrderByIdGivenOrderNotExist() {
    // Given
    UUID uuid = UUID.randomUUID();
    when(orderRepository.findById(uuid)).thenReturn(Optional.empty());

    // When
    // Then
    assertThrows(OrderNotFoundException.class, () -> findOrderService.findOrderById(uuid));
  }
}
