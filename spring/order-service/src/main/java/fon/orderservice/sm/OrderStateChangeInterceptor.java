package fon.orderservice.sm;

import fon.orderservice.domain.Order;
import fon.orderservice.domain.OrderEvent;
import fon.orderservice.domain.OrderStatus;
import fon.orderservice.repository.OrderRepository;
import fon.orderservice.service.order.OrderManagerImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@AllArgsConstructor
@Component
public class OrderStateChangeInterceptor extends StateMachineInterceptorAdapter<OrderStatus, OrderEvent> {

    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public void preStateChange(State<OrderStatus, OrderEvent> state, Message<OrderEvent> message, Transition<OrderStatus, OrderEvent> transition, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        log.debug("Pre-State Change");

        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((String) msg.getHeaders().getOrDefault(OrderManagerImpl.ORDER_ID_HEADER, " ")))
                .ifPresent(orderId -> {
                    log.debug("Saving state for order id: " + orderId + " Status: " + state.getId());

                    Order order = orderRepository.getOne(UUID.fromString(orderId));
                    order.setOrderStatus(state.getId());
                    orderRepository.saveAndFlush(order);
                });


    }
}
