package com.raksit.example.order.find.service;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import java.util.List;

public interface FindOrderService {
  List<OrderResponse> getOrdersBySource(String source) throws OrderNotFoundException;
}
