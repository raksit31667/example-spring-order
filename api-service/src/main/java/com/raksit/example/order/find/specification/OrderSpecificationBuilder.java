package com.raksit.example.order.find.specification;

import com.raksit.example.order.common.model.entity.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class OrderSpecificationBuilder {

  private static final String SQL_WILDCARD_PLACEHOLDER = "%%%s%%";

  public Specification<Order> buildCriteria(String keyword) {
    return sourceMatches(keyword).or(destinationMatches(keyword));
  }

  private Specification<Order> sourceMatches(String keyword) {
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("source")),
            String.format(SQL_WILDCARD_PLACEHOLDER, keyword.trim().toLowerCase()));
  }

  private Specification<Order> destinationMatches(String keyword) {
    return (root, query, cb) ->
        cb.like(cb.lower(root.get("destination")),
            String.format(SQL_WILDCARD_PLACEHOLDER, keyword.trim().toLowerCase()));
  }
}
