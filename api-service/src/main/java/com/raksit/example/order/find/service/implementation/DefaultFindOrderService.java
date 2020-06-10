package com.raksit.example.order.find.service.implementation;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.find.service.FindOrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DefaultFindOrderService implements FindOrderService {

  private final OrderRepository orderRepository;

  private final OrderMapper orderMapper;

  public DefaultFindOrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
  }

  @Override
  public List<OrderResponse> findOrdersBySource(String source) {
    List<Order> orders = orderRepository.findAllBySource(source);
    return orders.stream().map(orderMapper::orderToOrderResponse)
        .collect(Collectors.toList());
  }

  @Override
  public OrderResponse findOrderById(UUID id) {
    return orderRepository.findById(id)
        .map(orderMapper::orderToOrderResponse)
        .orElseThrow(OrderNotFoundException::new);
  }
}
