package com.raksit.example.order.find.service.implementation;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.find.service.FindOrderService;
import com.raksit.example.order.find.specification.OrderSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultFindOrderService implements FindOrderService {

  private final OrderRepository orderRepository;

  private final OrderSpecificationBuilder orderSpecificationBuilder;

  private final OrderMapper orderMapper;

  @Override
  public List<OrderResponse> findOrdersBySource(String source) {
    List<Order> orders = orderRepository.findAllBySource(source);
    return orders.stream().map(orderMapper::orderToOrderResponse)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderResponse> findOrdersByKeyword(String keyword) {
    return orderRepository.findAll(orderSpecificationBuilder.buildCriteria(keyword))
        .stream().map(orderMapper::orderToOrderResponse)
        .collect(Collectors.toList());
  }

  @Override
  public OrderResponse findOrderById(UUID id) {
    return orderRepository.findById(id)
        .map(orderMapper::orderToOrderResponse)
        .orElseThrow(OrderNotFoundException::new);
  }
}
