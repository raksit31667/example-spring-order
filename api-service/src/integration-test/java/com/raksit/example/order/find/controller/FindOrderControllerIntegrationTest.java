package com.raksit.example.order.find.controller;

import com.raksit.example.order.IntegrationTest;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FindOrderControllerIntegrationTest extends IntegrationTest {

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  void shouldReturnOrdersWithBangkokSourceWhenFindOrdersBySourceGivenSourceBangkok() {
    // Given
    Order thaiOrder = Order.builder()
        .source("Bangkok")
        .destination("Houston")
        .items(newArrayList())
        .build();
    Order chineseOrder = Order.builder()
        .source("Wuhan")
        .destination("Houston")
        .items(newArrayList())
        .build();

    orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);

    // When
    UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString("/orders").queryParam("source", "Bangkok");
    ResponseEntity<Order[]> responseEntity =
        restTemplate.getForEntity(uriBuilder.build().toString(), Order[].class);

    // Then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    List<Order> actualOrders = asList(responseEntity.getBody());
    assertThat(actualOrders, hasSize(1));
    assertThat(actualOrders, everyItem(hasProperty("source", is("Bangkok"))));
  }

  @Test
  void shouldReturnEmptyOrderWhenFindOrdersBySourceGivenOrdersWithSourceBangkokNotFound() {
    // Given
    Order someOrder = Order.builder()
        .source("Somewhere")
        .destination("Houston")
        .items(newArrayList())
        .build();
    orderRepository.save(someOrder);

    // When
    UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString("/orders").queryParam("source", "Bangkok");
    ResponseEntity<Order[]> responseEntity =
        restTemplate.getForEntity(uriBuilder.build().toString(), Order[].class);

    // Then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    List<Order> actualOrders = asList(responseEntity.getBody());
    assertThat(actualOrders, hasSize(0));
  }
}
