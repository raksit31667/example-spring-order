package com.raksit.example.order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import io.restassured.http.ContentType;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class CreateOrderControllerIsolationTest extends IsolationTest {

  @Test
  public void createOrder_ShouldReturnOrderDtoWithNumberOfItemsAndTotalPrice() {
    OrderLineItem orderLineItem = OrderLineItem.builder()
        .name("Diesel")
        .price(2000.0)
        .build();

    Order order = Order.builder()
        .source("Bangkok")
        .destination("Houston")
        .items(Collections.nCopies(3, orderLineItem))
        .build();

    given().contentType(ContentType.JSON)
        .body(order)
        .when().post("/")
        .then()
        .statusCode(200)
        .body("source", is("Bangkok"))
        .body("destination", is("Houston"))
        .body("numberOfItems", is(3))
        .body("totalPrice", equalTo(6000.0f));
  }
}
