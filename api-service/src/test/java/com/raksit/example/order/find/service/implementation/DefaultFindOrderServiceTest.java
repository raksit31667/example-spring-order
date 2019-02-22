package com.raksit.example.order.find.service.implementation;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DefaultFindOrderServiceTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @InjectMocks private DefaultFindOrderService findOrderService;

  @Mock private OrderRepository orderRepository;

  @Test
  public void getOrdersBySource_ShouldReturnOrdersWithSpecificSource() {
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    when(orderRepository.findAllBySource(eq("Bangkok")))
        .thenReturn(Optional.of(Collections.singletonList(thaiOrder)));

    Optional<List<Order>> actualOrders = findOrderService.getOrdersBySource("Bangkok");

    assertTrue(actualOrders.isPresent());
    assertEquals(thaiOrder, actualOrders.get().iterator().next());
  }
}
