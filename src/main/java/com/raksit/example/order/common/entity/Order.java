package com.raksit.example.order.common.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "sales_order")
@Data
@Builder
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String source;

  private String destination;

  @OneToMany(mappedBy = "order")
  private List<OrderLineItem> items;
}
