package com.raksit.example.order.find.service.implementation;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.find.service.FindOrderService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultFindOrderService implements FindOrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderMapper orderMapper;

  @Override
  public List<OrderResponse> findOrdersBySource(String source) throws OrderNotFoundException {
    List<Order> orders = orderRepository.findAllBySource(source)
        .orElseThrow(OrderNotFoundException::new);
    return orders.stream().map(order -> orderMapper.orderToOrderResponse(order))
        .collect(Collectors.toList());
  }

  @Override
  public OrderResponse findOrderById(Long id) {
    return null;
  }
}
