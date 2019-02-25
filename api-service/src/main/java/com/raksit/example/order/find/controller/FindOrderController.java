package com.raksit.example.order.find.controller;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.find.service.FindOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Order"})
public class FindOrderController {

  @Autowired
  private FindOrderService findOrderService;

  @GetMapping("/orders")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Get Order By Source (Sold-to)")
  public List<Order> getOrdersBySource(@RequestParam String source) throws Exception {
    return findOrderService.getOrdersBySource(source);
  }
}
