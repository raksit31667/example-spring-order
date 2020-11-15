package com.raksit.example.order.create;

import com.raksit.example.order.KafkaIntegrationTest;
import com.raksit.example.order.common.model.dto.OrderLineItemRequest;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.repository.OrderRepository;
import io.restassured.http.ContentType;
import java.util.Collections;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

class CreateOrderIntegrationTest extends KafkaIntegrationTest {

  @Autowired
  private OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest() {
    OrderRequest orderRequest = buildOrderRequest();

    givenRequestWithValidWriteToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("id", notNullValue())
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
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("message", is("items must not be empty"));
  }

  @Test
  void shouldReturnForbiddenWhenCreateOrderGivenTokenWithReadRole() {
    OrderRequest orderRequest = buildOrderRequest();

    givenRequestWithValidReadToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .body("message", is("The server understood the request " +
            "but refuses to authorize it"));
  }

  @Test
  void shouldReturnUnauthorizedWhenCreateOrderGivenNoToken() {
    OrderRequest orderRequest = buildOrderRequest();

    given()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnUnauthorizedWhenCreateOrderGivenInvalidToken() {
    OrderRequest orderRequest = buildOrderRequest();

    givenRequestWithInvalidToken()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnUnauthorizedWhenCreateOrderGivenBasicAuthentication() {
    OrderRequest orderRequest = buildOrderRequest();

    givenRequestWithBasicAuthentication()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnForbiddenWhenCreateOrderGivenInvalidIssuer() {
    OrderRequest orderRequest = buildOrderRequest();

    givenRequestWithInvalidIssuer()
        .contentType(ContentType.JSON)
        .body(orderRequest)
        .when()
        .post("/orders")
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .body("message", is("The server understood the request " +
            "but refuses to authorize it"));
  }

  private OrderRequest buildOrderRequest() {
    OrderLineItemRequest orderLineItemRequest = OrderLineItemRequest.builder()
        .name("Diesel")
        .price(2000.0)
        .currency("THB")
        .build();

    return OrderRequest.builder()
        .soldTo("Bangkok")
        .shipTo("Houston")
        .items(Collections.nCopies(3, orderLineItemRequest))
        .build();
  }
}
