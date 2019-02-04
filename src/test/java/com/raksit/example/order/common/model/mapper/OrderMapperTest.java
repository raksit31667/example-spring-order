package com.raksit.example.order.common.model.mapper;

import static org.junit.Assert.assertEquals;

import com.raksit.example.order.common.model.dto.OrderDto;
import com.raksit.example.order.common.model.entity.Order;
import com.raksit.example.order.common.util.MockOrderFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderMapperTest {

  private static final int NUMBER_OF_ITEMS = 3;

  @Test
  public void orderToOrderDto() {
    Order order = MockOrderFactory.createSampleOrder(NUMBER_OF_ITEMS);

    OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(order);

    assertEquals(order.getSource(), orderDto.getSource());
    assertEquals(order.getDestination(), orderDto.getDestination());
    assertEquals(NUMBER_OF_ITEMS, orderDto.getNumberOfItems());
    assertEquals(order.getTotalPrice(), orderDto.getTotalPrice(), 0);
  }
}
