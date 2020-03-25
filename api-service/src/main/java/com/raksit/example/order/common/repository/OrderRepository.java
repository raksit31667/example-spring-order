package com.raksit.example.order.common.repository;

import com.raksit.example.order.common.model.entity.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
  List<Order> findAllBySource(String source);
}
