package com.raksit.example.order.create;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.KafkaIntegrationTest;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import io.restassured.http.ContentType;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.KafkaException;

import static com.google.common.collect.Lists.newArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

class FailedKafkaCreateOrderIntegrationTest extends IntegrationTest {

  @Autowired
  private OrderRepository orderRepository;

  @MockBean
  private CreateOrderService createOrderService;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrderExceptionResponseWhenCreateOrderGivenOrderKafkaMessageCannotGetPublished() {
    OrderLineItemRequest orderLineItemRequest = OrderLineItemRequest.builder()
        .name("Diesel")
        .price(2000.0)
        .currency("THB")
        .build();

    OrderRequest orderRequest = OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(Collections.nCopies(3, orderLineItemRequest))
        .build();

    Mockito.when(createOrderService.createOrder(orderRequest)).thenThrow(new KafkaException(""));

    givenRequestWithValidWriteToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(500)
        .body("message", is("Cannot send order notification"));
  }

  @Test
  void shouldReturnBadRequestWhenCreateOrderGivenOrderLineItemHasInvalidCurrency() {
    OrderRequest orderRequest = buildOrderRequest();

    givenRequestWithValidWriteToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(400)
        .body("message", is("invalid currency"));
  }

  private OrderRequest buildOrderRequest() {
    OrderLineItemRequest orderLineItemRequest = OrderLineItemRequest.builder()
        .name("Diesel")
        .price(2000.0)
        .currency("555")
        .build();

    return OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(Collections.nCopies(3, orderLineItemRequest))
        .build();
  }
}
