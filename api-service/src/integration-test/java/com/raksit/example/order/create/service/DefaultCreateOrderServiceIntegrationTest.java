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
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class DefaultCreateOrderServiceIntegrationTest extends KafkaIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private CreateOrderService createOrderService;

  @Autowired private OrderRepository orderRepository;

  public Consumer<Integer, String> consumer;

  @BeforeEach
  void setUp() {
    Map<String, Object> consumerProps = KafkaTestUtils
        .consumerProps("test-group", "true", embeddedKafkaBroker);
    consumerProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    ConsumerFactory<Integer, String> consumerFactory = new DefaultKafkaConsumerFactory<>(
        consumerProps, new IntegerDeserializer(), new StringDeserializer());
    consumer = consumerFactory.createConsumer();
  }

  @AfterEach
  void tearDown() {
    consumer.close();
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
    assertAll("orderResponse", () -> {
      assertThat(orderResponse.getId(), notNullValue());
      assertThat(orderResponse.getSource(), equalTo("Bangkok"));
      assertThat(orderResponse.getDestination(), equalTo("Houston"));
      assertThat(orderResponse.getNumberOfItems(), equalTo(NUMBER_OF_ITEMS));
      assertThat(orderResponse.getTotalPrice(), equalTo(3000.0));
    });

    // Kafka
    consumer.subscribe(newArrayList("order.created"));
    ConsumerRecord<Integer, String> consumerRecord = KafkaTestUtils
        .getSingleRecord(consumer, "order.created");
    ObjectMapper objectMapper = new ObjectMapper();
    OrderKafkaMessage orderKafkaMessage = objectMapper
        .readValue(consumerRecord.value(), OrderKafkaMessage.class);
    assertThat(orderKafkaMessage.getOrderId(), notNullValue());
  }
}
