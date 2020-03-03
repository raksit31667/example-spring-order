package com.raksit.example.order.create.controller;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.create.service.CreateOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Order"})
public class CreateOrderController {

  @Autowired private CreateOrderService createOrderService;

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create an order")
  public OrderResponse createOrder(@RequestBody @Valid OrderRequest orderRequest) {
    return createOrderService.createOrder(orderRequest);
  }
}
