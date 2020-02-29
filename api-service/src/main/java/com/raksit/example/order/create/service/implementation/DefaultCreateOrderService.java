package com.raksit.example.order.create.service.implementation;

import com.raksit.example.order.common.model.dto.OrderKafkaMessage;
import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.model.mapper.OrderMapper;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.create.service.CreateOrderService;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import static com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly;

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
