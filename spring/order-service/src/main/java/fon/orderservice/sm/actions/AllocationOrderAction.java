package fon.orderservice.sm.actions;

import fon.orderservice.domain.Order;
import fon.orderservice.domain.OrderEvent;
import fon.orderservice.domain.OrderStatus;
import fon.orderservice.kafka.SourceBean;
import fon.orderservice.repository.OrderRepository;
import fon.orderservice.service.order.OrderManagerImpl;
import fon.orderservice.web.mapper.OrderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Component
public class AllocationOrderAction implements Action<OrderStatus, OrderEvent> {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final SourceBean sourceBean;

    @Override
    public void execute(StateContext<OrderStatus, OrderEvent> stateContext) {
        String orderId = (String) stateContext.getMessage().getHeaders().get(OrderManagerImpl.ORDER_ID_HEADER);
        log.debug("AllocationOrderAction for orderId {}", orderId);

        Optional<Order> optionalOrder = orderRepository.findById(UUID.fromString(orderId));
        optionalOrder.ifPresentOrElse(order -> {
            sourceBean.publishOrderAllocationEvent(orderMapper.orderToOrderDto(order));
            log.debug("Sent allocation request to AllocationOrderTopic for orderId {}", orderId);
        }, () -> log.error("Order Not Found."));
    }
}
