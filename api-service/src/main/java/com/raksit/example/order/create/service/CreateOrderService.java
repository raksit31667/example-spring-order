package com.raksit.example.order.create.service;

import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;

public interface CreateOrderService {
  OrderResponse createOrder(Order order);
}
