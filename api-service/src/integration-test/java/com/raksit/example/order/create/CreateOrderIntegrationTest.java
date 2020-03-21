package com.raksit.example.order.create;

import com.raksit.example.order.KafkaIntegrationTest;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.repository.OrderRepository;
import io.restassured.http.ContentType;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

class CreateOrderIntegrationTest extends KafkaIntegrationTest {

  @Autowired
  private OrderRepository orderRepository;

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

    givenRequestWithValidWriteToken()
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

  @Test
  void shouldReturnBadRequestWhenCreateOrderGivenOrderLineItemEmpty() {
    OrderRequest orderRequest = OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(newArrayList())
        .build();

    givenRequestWithValidWriteToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(400)
        .body("message", is("items must not be empty"));
  }

  @Test
  void shouldReturnForbiddenWhenCreateOrderGivenTokenWithReadRole() {
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

    givenRequestWithValidReadToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(403);
  }

  @Test
  void shouldReturnUnauthorizedWhenCreateOrderGivenNoToken() {
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
        .statusCode(401);
  }

  @Test
  void shouldReturnUnauthorizedWhenCreateOrderGivenInvalidToken() {
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

    givenRequestWithInvalidToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(401);
  }
}
