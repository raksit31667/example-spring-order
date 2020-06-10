package com.raksit.example.order.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

  private String id;
  private String source;
  private String destination;
  private Integer numberOfItems;
  private Double totalPrice;
  private List<String> currencies;
}
