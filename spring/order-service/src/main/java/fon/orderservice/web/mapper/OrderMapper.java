package fon.orderservice.web.mapper;

import fon.orderservice.domain.Order;
import fon.orderservice.web.model.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(uses = {DateMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    @Mappings({
    @Mapping(target = "customer.uuid", source = "customer_uuid"),
    @Mapping(target = "customer.email", source = "customerEmail")
    })
    Order orderDtoToOrder(OrderDto orderDto);

    @Mappings({
            @Mapping(target = "customer_uuid", source = "customer.uuid"),
            @Mapping(target = "customerEmail", source = "customer.email")
    })
    OrderDto orderToOrderDto(Order order);
}
