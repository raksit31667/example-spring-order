package com.raksit.example.order.create.service.implementation;

import com.raksit.example.order.common.model.dto.OrderKafkaMessage;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Money;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Collections;
import java.util.Currency;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCreateOrderServiceTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private OrderMapper orderMapper;

  @Mock
  private ListenableFuture listenableFuture;

  @Mock
  private KafkaTemplate<Integer, OrderKafkaMessage> kafkaTemplate;

  @InjectMocks
  private DefaultCreateOrderService createOrderService;

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest() {
    // Given
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);
    Order order = Order.builder()
        .source(orderRequest.getSoldTo())
        .destination(orderRequest.getShipTo())
        .items(Collections.nCopies(NUMBER_OF_ITEMS, OrderLineItem.builder()
            .money(new Money(1000.0, Currency.getInstance("THB")))
            .build()))
        .build();
    UUID uuid = UUID.randomUUID();
    Order savedOrder = Order.builder()
        .id(uuid)
        .source(order.getSource())
        .destination(order.getDestination())
        .items(order.getItems())
        .build();
    when(orderMapper.orderRequestToOrder(orderRequest)).thenReturn(order);
    when(orderRepository.save(order)).thenReturn(savedOrder);
    when(kafkaTemplate.send("order.created", 1, OrderKafkaMessage.builder().orderId(uuid).build()))
        .thenReturn(listenableFuture);
    when(orderMapper.orderToOrderResponse(savedOrder)).thenReturn(OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(NUMBER_OF_ITEMS)
        .totalPrice(3000.0)
        .build());

    // When
    OrderResponse orderResponse = createOrderService.createOrder(orderRequest);

    // Then
    assertEquals(orderRequest.getSoldTo(), orderResponse.getSource());
    assertEquals(orderRequest.getShipTo(), orderResponse.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems(), 0);
    assertEquals(3000.0, orderResponse.getTotalPrice(), 0);
  }

  @Test
  void shouldDeleteOrderAndThrowExceptionWhenCreateOrderGivenOrderKafkaMessageCannotGetPublished() {
    // Given
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);
    Order order = Order.builder().build();
    UUID uuid = UUID.randomUUID();
    Order savedOrder = Order.builder().id(uuid).build();
    when(orderMapper.orderRequestToOrder(orderRequest)).thenReturn(order);
    when(orderRepository.save(order)).thenReturn(savedOrder);

    SettableListenableFuture<SendResult<Integer, OrderKafkaMessage>> producerResult
        = new SettableListenableFuture<>();
    producerResult.setException(new ExecutionException("cannot publish order creation message",
        new RuntimeException()));
    when(kafkaTemplate.send("order.created", 1, OrderKafkaMessage.builder().orderId(uuid).build()))
        .thenReturn(producerResult);

    // When
    // Then
    assertThrows(KafkaException.class, () -> createOrderService.createOrder(orderRequest));
    verify(orderRepository).delete(savedOrder);
  }
}
