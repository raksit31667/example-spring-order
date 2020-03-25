package com.raksit.example.order.find.service.implementation;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultFindOrderServiceTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Mock private OrderRepository orderRepository;

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
    assertEquals("Bangkok", actualOrderResponse.getSource());
    assertEquals(thaiOrder.getDestination(), actualOrderResponse.getDestination());
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
    List<Object> expected = newArrayList();
    assertEquals(expected, actual);
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
    assertEquals(orderResponse, actual);
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
