package com.raksit.example.order.common.repository;

import com.raksit.example.order.common.model.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {}