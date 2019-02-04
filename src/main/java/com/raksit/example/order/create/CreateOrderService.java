package com.raksit.example.order.create;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import org.springframework.stereotype.Service;

@Service
public interface CreateOrderService {
  OrderDto createOrder(Order order);
}
