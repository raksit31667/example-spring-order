package com.raksit.example.order.create.controller.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CreateOrderControllerIntegrationTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private OrderRepository orderRepository;

  private HttpHeaders httpHeaders;

  @BeforeEach
  public void setUp() {
    httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
  }

  @AfterEach
  public void tearDown() {
    orderRepository.deleteAll();
  }

  @Test
  public void createOrder_ShouldReturnOrderDtoWithNumberOfItemsAndTotalPrice() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    HttpEntity<Order> httpEntity = new HttpEntity<>(order, httpHeaders);
    ResponseEntity<OrderDto> responseEntity = restTemplate.postForEntity("/", httpEntity, OrderDto.class);
    OrderDto orderDto = responseEntity.getBody();

    assertNotNull(orderDto);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertEquals(order.getSource(), orderDto.getSource());
    assertEquals(order.getDestination(), orderDto.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderDto.getNumberOfItems());
    assertEquals(PriceCalculator.calculateTotalPrice(order), orderDto.getTotalPrice(), 0);
  }
}
