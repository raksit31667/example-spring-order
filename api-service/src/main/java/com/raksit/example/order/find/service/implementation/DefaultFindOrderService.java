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

  @Override
  public List<OrderResponse> getOrdersBySource(String source) throws OrderNotFoundException {
    List<Order> orders = orderRepository.findAllBySource(source)
        .orElseThrow(OrderNotFoundException::new);

    return orders.stream().map(OrderMapper.INSTANCE::orderToOrderResponse)
        .collect(Collectors.toList());
  }
}
