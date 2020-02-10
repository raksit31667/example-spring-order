package com.raksit.example.order.common.model.entity;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sales_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String source;
  private String destination;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id", referencedColumnName = "id")
  private List<OrderLineItem> items;

  public Double getSubTotal() {
    return items.stream().mapToDouble(item -> item.getMoney().getPrice()).sum();
  }

  public Optional<Currency> getCurrency() {
    if (items.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(items.get(0).getMoney().getCurrency());
  }
}
