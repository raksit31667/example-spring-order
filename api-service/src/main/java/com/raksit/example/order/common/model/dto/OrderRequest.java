package com.raksit.example.order.common.model.dto;

import com.raksit.example.order.common.model.entity.OrderLineItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
  private String soldTo;
  private String shipTo;
  private List<OrderLineItem> items;
}
