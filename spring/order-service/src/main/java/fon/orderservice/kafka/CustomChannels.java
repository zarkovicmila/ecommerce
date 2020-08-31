package fon.orderservice.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface CustomChannels {


    String DEALLOCATE_ORDER_OUTPUT = "deallocateOrderOutput";
    String ALLOCATE_ORDER_OUTPUT = "allocateOrderOutput";
    String ALLOCATION_RESPONSE_INPUT = "allocationResponseInput";

    @Input("allocationResponseInput")
    SubscribableChannel allocationResponseInput();

    @Output("allocateOrderOutput")
    MessageChannel allocateOrderOutput();

    @Output("deallocateOrderOutput")
    MessageChannel deallocateOrderOutput();


}
