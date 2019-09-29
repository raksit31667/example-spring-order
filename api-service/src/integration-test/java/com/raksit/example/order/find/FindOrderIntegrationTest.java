package com.raksit.example.order.find;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.common.repository.OrderRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import static com.google.common.collect.Lists.newArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

class FindOrderIntegrationTest extends IntegrationTest {

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
  void shouldReturnOrdersWithBangkokSourceWhenFindOrdersBySourceGivenSourceBangkok() {
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .price(2000.0)
        .build();

    Order order = Order.builder()
            .source("Bangkok")
            .destination("Houston")
            .items(newArrayList(orderLineItem))
            .build();

    orderRepository.save(order);

    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders?source=Bangkok")
        .then()
        .statusCode(200)
        .body("[0].source", is("Bangkok"))
        .body("[0].destination", is("Houston"))
        .body("[0].numberOfItems", is(1))
        .body("[0].totalPrice", is(2000.0f));
  }
}
