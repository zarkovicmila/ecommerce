package fon.inventoryservice.service.order.listener;

import fon.inventoryservice.kafka.DeallocateOrderChannel;
import fon.inventoryservice.service.InventoryService;
import fon.inventoryservice.web.model.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableBinding(DeallocateOrderChannel.class)
public class DeallocateOrderListener {

    private final InventoryService inventoryService;

    @StreamListener(DeallocateOrderChannel.INPUT)
    public void listen(OrderDto orderDto) {
        log.debug("Received Deallocate Order Request for Order : {}", orderDto.getUuid());
        inventoryService.deallocateOrder(orderDto);
    }
}
