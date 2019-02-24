package com.raksit.example.order.find.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FindOrderController.class, secure = false)
public class FindOrderControllerTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private FindOrderService findOrderService;

  @Test
  public void getOrdersBySource_ShouldReturnOrdersWithSpecificSource() throws Exception {
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");

    when(findOrderService.getOrdersBySource(eq("Bangkok")))
        .thenReturn(Collections.singletonList(thaiOrder));

    mvc.perform(get("/orders")
    .param("source", "Bangkok"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].source", is("Bangkok")));
  }
}
