package fon.orderservice.service.order;

import fon.orderservice.domain.Order;
import fon.orderservice.domain.OrderEvent;
import fon.orderservice.domain.OrderItem;
import fon.orderservice.domain.OrderStatus;
import fon.orderservice.repository.OrderRepository;
import fon.orderservice.sm.OrderStateChangeInterceptor;
import fon.orderservice.web.model.OrderDto;
import fon.orderservice.web.model.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;



@Slf4j
@Service
@AllArgsConstructor
public class OrderManagerImpl implements OrderManager {

    public static final String ORDER_ID_HEADER = "ORDER_ID_HEADER";

    private final OrderRepository orderRepository;
    private final StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;
    private final OrderStateChangeInterceptor orderStateChangeInterceptor;

    @Transactional
    @Override
    public Order newOrder(Order order) {

        Order newOrder = orderRepository.saveAndFlush(order);

        log.debug("Order saved {}", order.getUuid());
        log.debug("Publishing Allocate Order Event");

        sendOrderEvent(order, OrderEvent.ALLOCATE_ORDER);

        return newOrder;
    }


    @Transactional
    @Override
    public void orderAllocationPassed(OrderDto orderDto) {
        Optional<Order> optionalOrder = orderRepository.findById(orderDto.getUuid());

        optionalOrder.ifPresentOrElse(order -> {
            updateAllocatedQuantity(order, orderDto);
            sendOrderEvent(order, OrderEvent.ALLOCATION_SUCCESS);
        }, () -> log.error("Order not found with id: {}", orderDto.getUuid()));
    }


    @Override
    public void orderAllocationFailed(OrderDto orderDto) {
        Optional<Order> optionalOrder = orderRepository.findById(orderDto.getUuid());

        optionalOrder.ifPresentOrElse(order -> {
            sendOrderEvent(order, OrderEvent.ALLOCATION_FAILED);
        }, () -> log.error("Order not found with id: {}", orderDto.getUuid()));
    }


    @Override
    public void cancelOrder(UUID id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        optionalOrder.ifPresentOrElse(order -> {
            sendOrderEvent(order, OrderEvent.CANCEL_ORDER);
        }, () -> log.error("Order not found with id: {}", id));
    }

    @Override
    public void orderPickedUp(UUID id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        optionalOrder.ifPresentOrElse(order -> {
            sendOrderEvent(order, OrderEvent.ORDER_PICKED_UP);
        }, () -> log.error("Order not found with id: {}", id));
    }

    @Override
    public void orderDelivered(UUID id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        optionalOrder.ifPresentOrElse(order -> {
            sendOrderEvent(order, OrderEvent.ORDER_DELIVERED);
        }, () -> log.error("Order not found with id: {}", id));
    }


    private void updateAllocatedQuantity(Order order, OrderDto orderDto) {
        log.debug("Updating allocated quantity for Order Id {}", orderDto.getUuid());

        BigDecimal totalOrder = BigDecimal.ZERO;

        for (OrderItem item:order.getOrderItems()) {
            for(OrderItemDto dto: orderDto.getOrderItems()){
                if(item.getProductCode().equals(dto.getProductCode())){
                    item.setQuantityAllocated(dto.getQuantityAllocated());
                    item.setSubtotal(dto.getPrice().multiply(new BigDecimal(dto.getQuantityAllocated())));
                    totalOrder = totalOrder.add(item.getSubtotal());
                    item.setOrder(order);
                }
            }
        }
        order.setOrderTotal(totalOrder);

        orderRepository.saveAndFlush(order);
    }

    public void sendOrderEvent(Order order, OrderEvent event) {
        StateMachine<OrderStatus, OrderEvent> sm = build(order);

        Message msg = MessageBuilder.withPayload(event)
                .setHeader(ORDER_ID_HEADER, order.getUuid().toString())
                .build();

        sm.sendEvent(msg);
    }

    public StateMachine<OrderStatus, OrderEvent> build(Order order) {
        StateMachine<OrderStatus, OrderEvent> sm = stateMachineFactory.getStateMachine(order.getUuid());

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(orderStateChangeInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(order.getOrderStatus(), null, null, null));
                });

        sm.start();

        return sm;
    }

}
