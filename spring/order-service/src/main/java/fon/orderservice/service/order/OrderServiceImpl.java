package fon.orderservice.service.order;

import fon.orderservice.domain.Customer;
import fon.orderservice.domain.Order;
import fon.orderservice.domain.OrderItem;
import fon.orderservice.domain.OrderStatus;
import fon.orderservice.repository.CustomerRepository;
import fon.orderservice.repository.OrderItemRepository;
import fon.orderservice.repository.OrderRepository;
import fon.orderservice.web.mapper.OrderMapper;
import fon.orderservice.web.model.OrderDto;
import fon.orderservice.web.model.OrderPagedList;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final OrderManager orderManager;

    @Override
    public OrderDto findOrderById(UUID uuid) {

        return orderMapper.orderToOrderDto(orderRepository.findById(uuid).orElseThrow(RuntimeException::new));
    }


    @Transactional
    @Override
    public OrderDto placeOrder(OrderDto orderDto) {
        Order order = orderMapper.orderDtoToOrder(orderDto);
        Customer customer = customerRepository.findCustomerByEmail(orderDto.getCustomerEmail());

        // if customer does not exist
        if(customer == null){
            throw new RuntimeException("Customer not found.");
        }

        order.setCustomer(customer);
        order.setUuid(null);
        order.setOrderStatus(OrderStatus.NEW);
        countOrderTotal(order);
        order.getOrderItems().forEach(orderItem -> orderItem.setOrder(order));

        Order newOrder = orderManager.newOrder(order);

        return orderMapper.orderToOrderDto(newOrder);
    }

    @Transactional
    @Override
    public void deleteOrder(UUID uuid) {
        orderRepository.deleteById(uuid);
    }

    @Override
    public OrderPagedList listOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return new OrderPagedList(orderPage
                .stream()
                .map(orderMapper::orderToOrderDto)
                .collect(Collectors.toList()), PageRequest.of(orderPage.getPageable().getPageNumber(), orderPage.getPageable().getPageSize()), orderPage.getTotalElements());

    }

    @Transactional
    @Override
    public OrderDto updateOrder(UUID orderId, OrderDto orderDto) {
        Order order = orderMapper.orderDtoToOrder(orderDto);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        optionalOrder.ifPresentOrElse(ord -> {

            ord.setOrderStatus(order.getOrderStatus());
            ord.setOrderTotal(order.getOrderTotal());

            Order updatedOrder = orderRepository.save(ord);

            orderItemRepository.deleteByOrder(ord);

            order.getOrderItems().forEach(orderItem -> {
                orderItem.setOrder(updatedOrder);
            });

            orderItemRepository.saveAll(order.getOrderItems());

        }, () ->  new RuntimeException("Order not found"));

        return orderDto;
    }

    private void countOrderTotal(Order order){
        BigDecimal totalOrder = BigDecimal.ZERO;

        for (OrderItem item : order.getOrderItems()) {
            totalOrder = totalOrder.add(item.getSubtotal());
        }
        order.setOrderTotal(totalOrder);
    }


}
