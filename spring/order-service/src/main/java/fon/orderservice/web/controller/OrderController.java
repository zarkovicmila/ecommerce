package fon.orderservice.web.controller;

import fon.orderservice.service.order.OrderManager;
import fon.orderservice.service.order.OrderService;
import fon.orderservice.web.model.OrderDto;
import fon.orderservice.web.model.OrderPagedList;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
@CrossOrigin
public class OrderController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 15;

    private final OrderService orderService;
    private final OrderManager orderManager;


    @GetMapping("order")
    public OrderPagedList listOrders(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return orderService.listOrders(PageRequest.of(pageNumber, pageSize));
    }

    @GetMapping("order/{orderId}")
    public ResponseEntity<OrderDto> findOrder(@PathVariable("orderId") UUID orderId) {
        return new ResponseEntity<>(orderService.findOrderById(orderId), HttpStatus.OK);
    }

    @PostMapping("order")
    public ResponseEntity placeOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity(orderService.placeOrder(orderDto), HttpStatus.CREATED);
    }

    @DeleteMapping("order/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable("orderId") UUID orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("order/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable("orderId") UUID orderId, @RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.updateOrder(orderId, orderDto), HttpStatus.OK);
    }

    @PutMapping("order/{orderId}/cancel")
    public ResponseEntity cancelOrder(@PathVariable("orderId") UUID orderId) {
        orderManager.cancelOrder(orderId);
        return new ResponseEntity<>("Order canceled.", HttpStatus.OK);
    }

    @PutMapping("order/{orderId}/pickup")
    public ResponseEntity pickupUpOrder(@PathVariable("orderId") UUID orderId) {
        orderManager.orderPickedUp(orderId);
        return new ResponseEntity<>("Order picked up.", HttpStatus.OK);
    }

    @PutMapping("order/{orderId}/deliver")
    public ResponseEntity deliverOrder(@PathVariable("orderId") UUID orderId) {
        orderManager.orderDelivered(orderId);
        return new ResponseEntity<>("Order delivered.", HttpStatus.OK);
    }
}
