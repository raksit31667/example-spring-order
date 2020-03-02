package com.raksit.example.order.create;

import com.raksit.example.order.KafkaIntegrationTest;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.repository.OrderRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static com.google.common.collect.Lists.newArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

class CreateOrderIntegrationTest extends KafkaIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private OrderRepository orderRepository;

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest() {
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

    given()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(201)
        .body("source", is("Bangkok"))
        .body("destination", is("Houston"))
        .body("numberOfItems", is(3))
        .body("totalPrice", equalTo(6000.0f))
        .body("currencies", is(newArrayList("THB")));
  }
}
