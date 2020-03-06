package com.raksit.example.order.create;

import com.raksit.example.order.KafkaIntegrationTest;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.kafka.KafkaException;

import static com.google.common.collect.Lists.newArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

class FailedCreateOrderIntegrationTest extends KafkaIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private OrderRepository orderRepository;

  @MockBean
  private CreateOrderService createOrderService;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

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

    given()
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
    OrderLineItemRequest orderLineItemRequest = OrderLineItemRequest.builder()
        .name("Diesel")
        .price(2000.0)
        .currency("555")
        .build();

    OrderRequest orderRequest = OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(Collections.nCopies(3, orderLineItemRequest))
        .build();

    given()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(400)
        .body("message", is("validCurrency"));
  }

  @Test
  void shouldReturnBadRequestWhenCreateOrderGivenOrderLineItemEmpty() {
    OrderRequest orderRequest = OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(newArrayList())
        .build();

    given()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(400)
        .body("message", is("items must not be empty"));
  }
}
