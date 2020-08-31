package fon.inventoryservice.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DeallocateOrderChannel {

    String INPUT = "deallocateOrderInput";

    @Input("deallocateOrderInput")
    SubscribableChannel deallocateOrderInput();
}
