package fon.orderservice.service.order;

import fon.orderservice.web.model.OrderDto;
import fon.orderservice.web.model.OrderPagedList;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    OrderDto findOrderById(UUID uuid);

    OrderDto placeOrder(OrderDto orderDto);

    void deleteOrder(UUID uuid);

    OrderDto updateOrder(UUID orderId, OrderDto orderDto);

    OrderPagedList listOrders(Pageable pageable);
}
