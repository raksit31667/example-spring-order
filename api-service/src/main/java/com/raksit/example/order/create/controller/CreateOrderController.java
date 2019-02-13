package com.raksit.example.order.create.controller;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.create.service.CreateOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateOrderController {

  @Autowired
  private CreateOrderService createOrderService;

  @PostMapping("/")
  @ResponseStatus(HttpStatus.OK)
  public OrderDto createOrder(@RequestBody Order order) {
    return createOrderService.createOrder(order);
  }
}
