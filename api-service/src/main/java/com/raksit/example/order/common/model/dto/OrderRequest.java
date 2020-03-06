package com.raksit.example.order.common.model.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
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
  @NotEmpty(message = "items must not be empty")
  private List<OrderLineItemRequest> items;
}
