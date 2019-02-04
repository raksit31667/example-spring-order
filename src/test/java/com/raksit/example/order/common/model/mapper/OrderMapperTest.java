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

  @Test
  public void orderToOrderDto() {
    Order order = MockOrderFactory.createSampleOrder();

    OrderDto orderDto = OrderMapper.INSTANCE.orderToOrderDto(order);

    assertEquals(order.getSource(), orderDto.getSource());
    assertEquals(order.getDestination(), orderDto.getDestination());
    assertEquals(3, orderDto.getNumberOfItems());
    assertEquals(6000, orderDto.getTotalPrice(), 0);
  }
}
