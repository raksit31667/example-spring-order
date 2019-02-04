package com.raksit.example.order.common.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

  private String source;
  private String destination;
  private int numberOfItems;
  private double totalPrice;
}
