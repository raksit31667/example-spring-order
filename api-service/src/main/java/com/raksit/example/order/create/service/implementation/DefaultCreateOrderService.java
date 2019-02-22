package com.raksit.example.order.create.service.implementation;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultCreateOrderService implements CreateOrderService {

  @Autowired private OrderRepository orderRepository;

  @Override
  public OrderResponse createOrder(OrderRequest orderRequest) {
    Order order = OrderMapper.INSTANCE.orderRequestToOrder(orderRequest);
    return OrderMapper.INSTANCE.orderToOrderResponse(orderRepository.save(order));
  }
}
