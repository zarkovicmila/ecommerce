package fon.orderservice.service.order;

import fon.orderservice.domain.Order;
import fon.orderservice.web.model.OrderDto;

import java.util.UUID;


public interface OrderManager {

        Order newOrder(Order order);
        void orderAllocationPassed(OrderDto orderDto);
        void orderAllocationFailed(OrderDto orderDto);
        void cancelOrder(UUID id);
        void orderPickedUp(UUID id);
        void orderDelivered(UUID id);
}
