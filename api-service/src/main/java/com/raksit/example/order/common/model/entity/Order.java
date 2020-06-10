package com.raksit.example.order.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "sales_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String source;
  private String destination;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  private List<OrderLineItem> items;

  public Double getSubTotal() {
    return items.stream().mapToDouble(item -> item.getMoney().getPrice()).sum();
  }

  public List<Currency> getCurrencies() {
    return items.stream().map(item -> item.getMoney().getCurrency()).distinct().collect(toList());
  }
}
