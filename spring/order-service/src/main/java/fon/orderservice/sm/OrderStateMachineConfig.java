package fon.orderservice.sm;


import fon.orderservice.domain.OrderEvent;
import fon.orderservice.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEvent> {

    private final Action<OrderStatus, OrderEvent> allocationOrderAction;
    private final Action<OrderStatus, OrderEvent> deallocateOrderAction;

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderStatus.NEW)
                .states(EnumSet.allOf(OrderStatus.class))
                .end(OrderStatus.PICKED_UP)
                .end(OrderStatus.DELIVERED)
                .end(OrderStatus.ALLOCATION_EXCEPTION)
                .end(OrderStatus.CANCELLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderStatus.NEW).target(OrderStatus.ALLOCATION_PENDING)
                .event(OrderEvent.ALLOCATE_ORDER)
                .action(allocationOrderAction)
                .and().withExternal()
                .source(OrderStatus.ALLOCATION_PENDING).target(OrderStatus.ALLOCATED)
                .event(OrderEvent.ALLOCATION_SUCCESS)
                .and().withExternal()
                .source(OrderStatus.ALLOCATION_PENDING).target(OrderStatus.ALLOCATION_EXCEPTION)
                .event(OrderEvent.ALLOCATION_FAILED)
                .and().withExternal()
                .source(OrderStatus.ALLOCATION_PENDING).target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .and().withExternal()
                .source(OrderStatus.ALLOCATED).target(OrderStatus.PICKED_UP)
                .event(OrderEvent.ORDER_PICKED_UP)
                .and().withExternal()
                .source(OrderStatus.ALLOCATED).target(OrderStatus.CANCELLED)
                .event(OrderEvent.CANCEL_ORDER)
                .action(deallocateOrderAction)
                .and().withExternal()
                .source(OrderStatus.ALLOCATED).target(OrderStatus.DELIVERED)
                .event(OrderEvent.ORDER_DELIVERED);
    }
}
