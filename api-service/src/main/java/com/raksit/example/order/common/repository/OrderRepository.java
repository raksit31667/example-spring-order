package com.raksit.example.order.common.repository;

import com.raksit.example.order.common.model.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
  List<Order> findAllBySource(String source);
}
