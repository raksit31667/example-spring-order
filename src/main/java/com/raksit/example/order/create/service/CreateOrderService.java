package com.raksit.example.order.create.service;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;

public interface CreateOrderService {
  OrderDto createOrder(Order order);
}
