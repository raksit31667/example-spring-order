package com.raksit.example.order.find.controller.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FindOrderControllerIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private OrderRepository orderRepository;

  @AfterEach
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  public void getOrdersBySource_ShouldReturnOrdersWithSpecificSource() throws Exception {
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    orderRepository.save(thaiOrder);
    orderRepository.save(chineseOrder);

    UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString("/orders").queryParam("source", "Bangkok");

    ResponseEntity<Order[]> responseEntity =
        restTemplate.getForEntity(uriBuilder.build().toString(), Order[].class);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<Order> actualOrders = Arrays.asList(responseEntity.getBody());
    assertThat(actualOrders, hasSize(1));
    assertThat(actualOrders, everyItem(hasProperty("source", is("Bangkok"))));
  }

  @Test
  public void getOrdersBySource_NotFound_ShouldReturnNotFound() throws Exception {
    Order someOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    someOrder.setSource("Somewhere");
    orderRepository.save(someOrder);

    UriComponentsBuilder uriBuilder =
        UriComponentsBuilder.fromUriString("/orders").queryParam("source", "Bangkok");

    ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uriBuilder.build().toString(), null, Object.class);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
  }
}
