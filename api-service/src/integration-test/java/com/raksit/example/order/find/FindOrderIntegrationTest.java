package com.raksit.example.order.find;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.common.model.entity.Money;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.common.repository.OrderRepository;
import io.restassured.http.ContentType;
import java.util.Currency;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

class FindOrderIntegrationTest extends IntegrationTest {

  @Autowired
  private OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrdersWithBangkokSourceWhenFindOrdersBySourceGivenSourceBangkok() {
    Order order = buildOrder();

    orderRepository.save(order);

    givenRequestWithValidReadToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders?source=Bangkok")
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("[0].source", is("Bangkok"))
        .body("[0].destination", is("Houston"))
        .body("[0].numberOfItems", is(1))
        .body("[0].totalPrice", is(2000.0f))
        .body("[0].currencies", is(newArrayList("THB")));
  }

  @Test
  void shouldReturnOrderWhenFindOrderByIdGivenOrderId() {
    Order order = buildOrder();

    Order savedOrder = orderRepository.save(order);

    givenRequestWithValidReadToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders/" + savedOrder.getId())
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("source", is("Bangkok"))
        .body("destination", is("Houston"))
        .body("numberOfItems", is(1))
        .body("totalPrice", is(2000.0f))
        .body("currencies", is(newArrayList("THB")));
  }

  @Test
  void shouldReturnForbiddenWhenFindOrdersBySourceGivenTokenWithWriteRole() {
    Order order = buildOrder();

    orderRepository.save(order);

    givenRequestWithValidWriteToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders?source=Bangkok")
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .body("message", is("The server understood the request " +
            "but refuses to authorize it"));
  }

  @Test
  void shouldReturnForbiddenWhenFindOrderByIdGivenTokenWithWriteRole() {
    Order order = buildOrder();

    Order savedOrder = orderRepository.save(order);

    givenRequestWithValidWriteToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders/" + savedOrder.getId())
        .then()
        .statusCode(HttpStatus.SC_FORBIDDEN)
        .body("message", is("The server understood the request " +
            "but refuses to authorize it"));
  }

  @Test
  void shouldReturnUnauthorizedWhenFindOrdersBySourceGivenNoToken() {
    Order order = buildOrder();

    orderRepository.save(order);

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders?source=Bangkok")
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnUnauthorizedWhenFindOrderByIdGivenNoToken() {
    Order order = buildOrder();

    Order savedOrder = orderRepository.save(order);

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders/" + savedOrder.getId())
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnUnauthorizedWhenFindOrdersBySourceGivenInvalidToken() {
    Order order = buildOrder();

    orderRepository.save(order);

    givenRequestWithInvalidToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders?source=Bangkok")
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnUnauthorizedWhenFindOrderByIdGivenInvalidToken() {
    Order order = buildOrder();

    Order savedOrder = orderRepository.save(order);

    givenRequestWithInvalidToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders/" + savedOrder.getId())
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnUnauthorizedWhenFindOrdersBySourceGivenBasicAuthentication() {
    Order order = buildOrder();

    orderRepository.save(order);

    givenRequestWithBasicAuthentication()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders?source=Bangkok")
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnUnauthorizedWhenFindOrderByIdGivenBasicAuthentication() {
    Order order = buildOrder();

    Order savedOrder = orderRepository.save(order);

    givenRequestWithBasicAuthentication()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders/" + savedOrder.getId())
        .then()
        .statusCode(HttpStatus.SC_UNAUTHORIZED)
        .body("message", is("The request has not been applied because " +
            "it lacks valid authentication credentials for the target resource"));
  }

  @Test
  void shouldReturnBadRequestWhenFindOrderByIdGivenInvalidOrderId() {
    givenRequestWithValidReadToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders/abcd")
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("message", is("invalid order id"));
  }

  private Order buildOrder() {
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .money(new Money(2000.0, Currency.getInstance("THB")))
        .build();

    return Order.builder()
        .source("Bangkok")
        .destination("Houston")
        .items(newArrayList(orderLineItem))
        .build();
  }
}
