package fon.inventoryservice.service.order.listener;


import fon.inventoryservice.kafka.SimpleSourceBean;
import fon.inventoryservice.service.InventoryService;
import fon.inventoryservice.web.model.OrderDto;
import fon.inventoryservice.web.model.event.AllocateOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableBinding(Sink.class)
public class AllocateOrderListener {

    private final SimpleSourceBean source;
    private final InventoryService inventoryService;

    @StreamListener(Sink.INPUT)
    public void listen(OrderDto orderDto) {
        log.debug("Received Allocate Order Request for Order : {}", orderDto.getUuid());
        AllocateOrderResponse response;
        try {
            OrderDto dto = inventoryService.allocateOrderInventory(orderDto);
            response = AllocateOrderResponse.builder().orderDto(dto).allocationError(false).build();
        } catch (Exception e) {
            response = AllocateOrderResponse.builder().allocationError(true).build();
            log.error("Allocation failed for Order : {}", orderDto.getUuid());
        }
        source.sendOrderAllocateResponse(response);
    }
}
