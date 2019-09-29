package com.raksit.example.order.find.controller;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.find.service.FindOrderService;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FindOrderController.class, secure = false)
class FindOrderControllerTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private MockMvc mvc;

  @MockBean private FindOrderService findOrderService;

  @Test
  void shouldReturnOrdersWithBangkokSourceWhenFindOrdersBySourceGivenSourceBangkok() throws Exception {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    order.setSource("Bangkok");

    OrderResponse orderResponse = OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .totalPrice(3000.0)
        .build();

    when(findOrderService.findOrdersBySource(eq("Bangkok")))
        .thenReturn(
            Collections.singletonList(orderResponse));

    mvc.perform(get("/orders").param("source", "Bangkok"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].source", is("Bangkok")));
  }

  @Test
  void shouldReturnStatusNotFoundWhenFindOrdersBySourceGivenOrdersWithSourceBangkokNotFound() throws Exception {
    when(findOrderService.findOrdersBySource(eq("Bangkok"))).thenThrow(new OrderNotFoundException());

    mvc.perform(get("/orders").param("source", "Bangkok"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Order Not Found"));
  }
}
