package com.raksit.example.order.find.controller;

import com.raksit.example.order.common.exception.OrderExceptionResponse;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.find.service.FindOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Api(tags = {"Order"})
public class FindOrderController {

  @Autowired private FindOrderService findOrderService;

  @GetMapping
  @ApiOperation("Get order by source (sold-to)")
  public List<OrderResponse> findOrdersBySource(@RequestParam String source) {
    return findOrderService.findOrdersBySource(source);
  }

  @GetMapping("/{orderId}")
  @ApiOperation("Get order by id")
  @ApiResponses(
      value = {
          @ApiResponse(
              code = 404,
              message = "Order Not Found",
              response = OrderExceptionResponse.class)
      })
  public OrderResponse findOrderById(@PathVariable Long orderId) {
    return findOrderService.findOrderById(orderId);
  }
}
