package com.raksit.example.order.find;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.common.model.entity.Money;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import com.raksit.example.order.common.repository.OrderRepository;
import io.restassured.http.ContentType;
import java.util.Currency;
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
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .money(new Money(2000.0, Currency.getInstance("THB")))
        .build();

    Order order = Order.builder()
            .source("Bangkok")
            .destination("Houston")
            .items(newArrayList(orderLineItem))
            .build();

    orderRepository.save(order);

    givenRequestWithValidReadToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders?source=Bangkok")
        .then()
        .statusCode(200)
        .body("[0].source", is("Bangkok"))
        .body("[0].destination", is("Houston"))
        .body("[0].numberOfItems", is(1))
        .body("[0].totalPrice", is(2000.0f))
        .body("[0].currencies", is(newArrayList("THB")));
  }

  @Test
  void shouldReturnOrderWhenFindOrderByIdGivenOrderId() {
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .money(new Money(2000.0, Currency.getInstance("THB")))
        .build();

    Order order = Order.builder()
        .source("Bangkok")
        .destination("Houston")
        .items(newArrayList(orderLineItem))
        .build();

    Order savedOrder = orderRepository.save(order);

    givenRequestWithValidReadToken()
        .contentType(ContentType.JSON)
        .when()
        .get("/orders/" + savedOrder.getId())
        .then()
        .statusCode(200)
        .body("source", is("Bangkok"))
        .body("destination", is("Houston"))
        .body("numberOfItems", is(1))
        .body("totalPrice", is(2000.0f))
        .body("currencies", is(newArrayList("THB")));
  }
}
