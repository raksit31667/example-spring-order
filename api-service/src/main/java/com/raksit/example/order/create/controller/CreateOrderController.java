package com.raksit.example.order.create.controller;

import com.raksit.example.order.common.exception.OrderExceptionResponse;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.create.service.CreateOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
@Api(tags = {"Order"})
public class CreateOrderController {

  private final CreateOrderService createOrderService;

  public CreateOrderController(CreateOrderService createOrderService) {
    this.createOrderService = createOrderService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Create an order")
  @PreAuthorize("hasApplicationRole('WRITE')")
  @ApiResponses({
      @ApiResponse(code = 400, message = "Bad Request", response = OrderExceptionResponse.class),
      @ApiResponse(code = 401, message = "Unauthorized"),
      @ApiResponse(code = 403, message = "Forbidden"),
      @ApiResponse(code = 500, message = "Internal Server Error")
  })
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "orderRequest", value = "order request body", required = true,
          paramType = "body", dataType = "OrderRequest")
  })
  public OrderResponse createOrder(@RequestBody @Valid OrderRequest orderRequest) {
    return createOrderService.createOrder(orderRequest);
  }
}
