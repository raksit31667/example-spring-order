package com.raksit.example.order.create.service.implementation;

import com.raksit.example.order.common.model.dto.OrderKafkaMessage;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class DefaultCreateOrderService implements CreateOrderService {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private OrderMapper orderMapper;

  @Autowired
  private KafkaTemplate<Integer, OrderKafkaMessage> kafkaTemplate;

  @Override
  public OrderResponse createOrder(OrderRequest orderRequest) {
    Order order = orderMapper.orderRequestToOrder(orderRequest);
    Order savedOrder = orderRepository.save(order);
    kafkaTemplate.send("order.created", 1, OrderKafkaMessage.builder()
        .orderId(savedOrder.getId())
        .build());
    return orderMapper.orderToOrderResponse(savedOrder);
  }
}
