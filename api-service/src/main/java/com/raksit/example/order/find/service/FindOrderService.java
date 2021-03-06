package com.raksit.example.order.find.service;

import com.raksit.example.order.common.model.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface FindOrderService {
  List<OrderResponse> findOrdersBySource(String source);

  List<OrderResponse> findOrdersByKeyword(String keyword);

  OrderResponse findOrderById(UUID id);

}
