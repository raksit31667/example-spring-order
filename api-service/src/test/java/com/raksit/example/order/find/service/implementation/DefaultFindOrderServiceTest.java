package com.raksit.example.order.find.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.raksit.example.order.common.exception.OrderNotFoundException;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.repository.OrderRepository;
import com.raksit.example.order.util.MockOrderFactory;
import java.util.Collections;
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
  public void getOrdersBySource_ShouldReturnOrdersWithSpecificSource() throws Exception {
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    when(orderRepository.findAllBySource(eq("Bangkok")))
        .thenReturn(Optional.of(Collections.singletonList(thaiOrder)));

    Order actualOrder = findOrderService.getOrdersBySource("Bangkok").iterator().next();

    assertEquals("Bangkok", actualOrder.getSource());
    assertEquals(thaiOrder.getDestination(), actualOrder.getDestination());
  }

  @Test
  public void getOrdersBySource_NotFound_ShouldThrowOrderNotFoundException() {
    Order thaiOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    Order chineseOrder = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);
    thaiOrder.setSource("Bangkok");
    chineseOrder.setSource("Wuhan");

    when(orderRepository.findAllBySource(eq("Houston")))
        .thenReturn(Optional.empty());

    assertThrows(OrderNotFoundException.class, () -> {
      findOrderService.getOrdersBySource("Houston");
    });
  }
}
