package com.raksit.example.order;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.entity.OrderLineItem;
import io.restassured.http.ContentType;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class CreateOrderControllerIsolationTest extends IsolationTest {

  @Test
  public void createOrder_ShouldReturnOrderDtoWithNumberOfItemsAndTotalPrice() {
    OrderLineItem orderLineItem = OrderLineItem.builder().name("Diesel").price(2000.0).build();

    OrderRequest orderRequest =
        OrderRequest.builder()
            .soldTo("Bangkok")
            .shipTo("Houston")
            .items(Collections.nCopies(3, orderLineItem))
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
        .body("totalPrice", equalTo(6000.0f));
  }
}
