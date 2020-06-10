package com.raksit.example.order.create.service.implementation;

import com.raksit.example.order.common.model.dto.OrderKafkaMessage;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import static com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly;

@Service
public class DefaultCreateOrderService implements CreateOrderService {

  private final OrderRepository orderRepository;

  private final OrderMapper orderMapper;

  private final KafkaTemplate<Integer, OrderKafkaMessage> kafkaTemplate;

  public DefaultCreateOrderService(OrderRepository orderRepository, OrderMapper orderMapper,
      KafkaTemplate<Integer, OrderKafkaMessage> kafkaTemplate) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public OrderResponse createOrder(OrderRequest orderRequest) {
    Order order = orderMapper.orderRequestToOrder(orderRequest);
    Order savedOrder = orderRepository.save(order);
    ListenableFuture<SendResult<Integer, OrderKafkaMessage>> listenableFuture = kafkaTemplate
        .send("order.created", 1, OrderKafkaMessage.builder()
            .orderId(savedOrder.getId())
            .build());

    try {
      getUninterruptibly(listenableFuture);
    } catch (ExecutionException e) {
      orderRepository.delete(savedOrder);
      throw new KafkaException("cannot publish order creation message");
    }
    return orderMapper.orderToOrderResponse(savedOrder);
  }
}
