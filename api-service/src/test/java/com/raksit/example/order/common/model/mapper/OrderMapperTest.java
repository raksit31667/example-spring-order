package com.raksit.example.order.common.model.mapper;

import static org.junit.Assert.assertEquals;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.util.MockOrderFactory;
import com.raksit.example.order.util.PriceCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderMapperTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  public void orderToOrderDto() {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(order);

    assertEquals(order.getSource(), orderDto.getSource());
    assertEquals(order.getDestination(), orderDto.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderDto.getNumberOfItems());
    assertEquals(PriceCalculator.calculateTotalPrice(order), orderDto.getTotalPrice(), 0);
  }
}
