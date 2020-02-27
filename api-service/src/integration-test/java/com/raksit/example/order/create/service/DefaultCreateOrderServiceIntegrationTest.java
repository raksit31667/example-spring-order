package com.raksit.example.order.create.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raksit.example.order.KafkaIntegrationTest;
import com.raksit.example.order.common.model.dto.OrderKafkaMessage;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.repository.OrderRepository;
import java.util.Collections;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

class DefaultCreateOrderServiceIntegrationTest extends KafkaIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private CreateOrderService createOrderService;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest()
      throws JsonProcessingException {
    // Given
    OrderRequest orderRequest = OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(Collections.nCopies(NUMBER_OF_ITEMS, OrderLineItemRequest.builder()
            .price(1000.0)
            .currency("USD")
            .build()))
        .build();

    // When
    OrderResponse orderResponse = createOrderService.createOrder(orderRequest);

    // Then
    assertEquals("Bangkok", orderResponse.getSource());
    assertEquals("Houston", orderResponse.getDestination());
    assertEquals("USD", orderResponse.getCurrency());
    assertEquals(NUMBER_OF_ITEMS, orderResponse.getNumberOfItems(), 0);
    assertEquals(3000.0, orderResponse.getTotalPrice(), 0);

    // Kafka
    consumer.subscribe(newArrayList("order.created"));
    ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils
        .getSingleRecord(consumer, "order.created");
    ObjectMapper objectMapper = new ObjectMapper();
    OrderKafkaMessage orderKafkaMessage = objectMapper
        .readValue(consumerRecord.value(), OrderKafkaMessage.class);
    assertEquals(1, orderKafkaMessage.getOrderId());
  }
}
