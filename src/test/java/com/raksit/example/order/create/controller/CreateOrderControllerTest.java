package com.raksit.example.order.create.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.create.service.CreateOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CreateOrderController.class, secure = false)
public class CreateOrderControllerTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private CreateOrderService createOrderService;

  @Test
  public void createOrder_ShouldReturnOrderDtoWithNumberOfItemsAndTotalPrice() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    OrderDto orderDto = OrderDto.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(NUMBER_OF_ITEMS)
        .totalPrice(PriceCalculator.calculateTotalPrice(order))
        .build();

    when(createOrderService.createOrder(order)).thenReturn(orderDto);


  }
}
