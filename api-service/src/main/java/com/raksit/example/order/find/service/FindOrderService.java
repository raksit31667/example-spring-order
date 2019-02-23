package com.raksit.example.order.find.service;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.entity.Order;
import java.util.List;

public interface FindOrderService {
    List<Order> getOrdersBySource(String source) throws OrderNotFoundException;
}
