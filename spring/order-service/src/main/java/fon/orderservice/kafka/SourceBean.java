package fon.orderservice.kafka;



import fon.orderservice.web.model.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@EnableBinding(CustomChannels.class)
public class SourceBean {

    private CustomChannels source;

    @Autowired
    public SourceBean(CustomChannels source) {
        this.source = source;
    }

    public void publishOrderAllocationEvent(OrderDto orderDto){
        log.debug("Sending message to OrderAllocationTopic for order {}", orderDto.getUuid().toString());
        source.allocateOrderOutput().send(MessageBuilder.withPayload(orderDto).build());
    }

    public void publishDeallocateOrderEvent(OrderDto orderDto) {
        log.debug("Sending message to DeallocateOrderTopic for order {}", orderDto.getUuid().toString());
        source.deallocateOrderOutput().send(MessageBuilder.withPayload(orderDto).build());
    }
}
