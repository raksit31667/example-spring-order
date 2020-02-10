package com.raksit.example.order.common.model.entity;

import java.util.Currency;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Money {

  private Double price;
  private Currency currency;
}
