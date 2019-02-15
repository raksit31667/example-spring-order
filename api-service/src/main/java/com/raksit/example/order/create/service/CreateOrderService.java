package com.raksit.example.order.create.service;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;

public interface CreateOrderService {
  OrderResponse createOrder(OrderRequest orderRequest);
}
