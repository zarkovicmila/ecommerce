package fon.orderservice.web.mapper;

import fon.orderservice.domain.OrderItem;
import fon.orderservice.web.mapper.DateMapper;
import fon.orderservice.web.mapper.OrderItemDecoratorMapper;
import fon.orderservice.web.model.OrderItemDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;


@Mapper(uses = {DateMapper.class})
@DecoratedWith(OrderItemDecoratorMapper.class)
public interface OrderItemMapper {

    OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto);

    OrderItemDto orderItemToOrderItemDto(OrderItem orderItem);
}
