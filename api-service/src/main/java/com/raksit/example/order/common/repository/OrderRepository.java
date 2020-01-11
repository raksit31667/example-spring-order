package com.raksit.example.order.common.repository;

import com.raksit.example.order.common.model.entity.Order;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
  List<Order> findAllBySource(String source);
}
