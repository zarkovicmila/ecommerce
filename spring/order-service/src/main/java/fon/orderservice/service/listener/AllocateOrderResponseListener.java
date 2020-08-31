package fon.orderservice.service.listener;


import fon.orderservice.kafka.CustomChannels;
import fon.orderservice.service.order.OrderManager;
import fon.orderservice.web.model.event.AllocateOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@RequiredArgsConstructor
@Slf4j
@EnableBinding(CustomChannels.class)
public class AllocateOrderResponseListener {

    private final OrderManager orderManager;

    @StreamListener(CustomChannels.ALLOCATION_RESPONSE_INPUT)
    public void listen(AllocateOrderResponse response) {
        log.debug("Allocation order response received for Order Id : {} ", response.getOrderDto().getUuid());

            if (!response.getAllocationError()) {
                orderManager.orderAllocationPassed(response.getOrderDto());
            } else {
                orderManager.orderAllocationFailed(response.getOrderDto());
            }
    }
}
