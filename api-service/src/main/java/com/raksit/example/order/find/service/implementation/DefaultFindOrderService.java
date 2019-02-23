package com.raksit.example.order.find.service.implementation;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.find.service.FindOrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultFindOrderService implements FindOrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Override
  public List<Order> getOrdersBySource(String source) throws OrderNotFoundException {
    return orderRepository.findAllBySource(source).orElseThrow(OrderNotFoundException::new);
  }
}
