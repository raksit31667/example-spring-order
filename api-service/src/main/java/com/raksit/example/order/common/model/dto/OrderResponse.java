package com.raksit.example.order.common.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

  private String source;
  private String destination;
  private Integer numberOfItems;
  private Double totalPrice;
  private List<String> currencies;
}
