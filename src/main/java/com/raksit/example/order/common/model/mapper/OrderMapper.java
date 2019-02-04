package com.raksit.example.order.common.model.mapper;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class OrderMapper {

  public static final OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  public OrderDto orderToOrderDto(Order order) {
    return OrderDto.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(order.getItems().size())
        .totalPrice(order.getTotalPrice())
        .build();
  }
}
