package com.raksit.example.order.find.controller;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.find.service.FindOrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FindOrderController {

  @Autowired
  private FindOrderService findOrderService;

  @GetMapping("/orders")
  @ResponseStatus(HttpStatus.OK)
  public List<Order> getOrdersBySource(@RequestParam String source) throws Exception {
    return findOrderService.getOrdersBySource(source);
  }
}
