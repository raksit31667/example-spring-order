package com.raksit.example.order.common.model.dto;

import java.util.List;
import javax.validation.Valid;
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

  @Valid
  private List<OrderLineItemRequest> items;
}
