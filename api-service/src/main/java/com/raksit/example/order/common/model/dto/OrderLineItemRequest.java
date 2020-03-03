package com.raksit.example.order.common.model.dto;

import com.raksit.example.order.validator.ValidCurrency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItemRequest {

  private String name;
  private Double price;

  @ValidCurrency
  private String currency;
}
