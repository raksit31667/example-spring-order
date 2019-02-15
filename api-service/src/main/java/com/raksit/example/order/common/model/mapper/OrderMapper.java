package com.raksit.example.order.common.model.mapper;

import com.raksit.example.order.common.model.dto.OrderRequest;
import com.raksit.example.order.common.model.dto.OrderResponse;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.util.PriceCalculator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class OrderMapper {

  public static final OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  @Mapping(source = "soldTo", target = "source")
  @Mapping(source = "shipTo", target = "destination")
  public abstract Order orderRequestToOrder(OrderRequest orderRequest);

  public OrderResponse orderToOrderResponse(Order order) {
    return OrderResponse.builder()
        .source(order.getSource())
        .destination(order.getDestination())
        .numberOfItems(order.getItems().size())
        .totalPrice(PriceCalculator.calculateTotalPrice(order.getItems()))
        .build();
  }
}
