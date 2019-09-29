package com.raksit.example.order.create.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.create.service.CreateOrderService;
import com.raksit.example.order.util.JsonConverter;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CreateOrderController.class, secure = false)
class CreateOrderControllerTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired private MockMvc mvc;

  @MockBean private CreateOrderService createOrderService;

  @Test
  void shouldReturnOrderResponseWithNumberOfItemsAndTotalPriceWhenCreateOrderGivenOrderRequest() throws Exception {
    OrderRequest orderRequest = MockOrderFactory.createSampleOrderRequest(NUMBER_OF_ITEMS);
    OrderResponse orderResponse =
        OrderResponse.builder()
            .source(orderRequest.getSoldTo())
            .destination(orderRequest.getShipTo())
            .numberOfItems(NUMBER_OF_ITEMS)
            .totalPrice(PriceCalculator.calculateTotalPrice(orderRequest.getItems()))
            .build();

    when(createOrderService.createOrder(any(OrderRequest.class))).thenReturn(orderResponse);

    mvc.perform(
            post("/orders")
                .content(JsonConverter.convertObjectToJsonString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.source", is(orderRequest.getSoldTo())))
        .andExpect(jsonPath("$.destination", is(orderRequest.getShipTo())))
        .andExpect(jsonPath("$.numberOfItems", is(NUMBER_OF_ITEMS)))
        .andExpect(
            jsonPath(
                "$.totalPrice", is(PriceCalculator.calculateTotalPrice(orderRequest.getItems()))));
  }
}
