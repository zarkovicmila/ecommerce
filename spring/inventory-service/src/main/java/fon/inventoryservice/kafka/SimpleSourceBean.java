package fon.inventoryservice.kafka;

import fon.inventoryservice.web.model.event.AllocateOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableBinding(Source.class)
public class SimpleSourceBean {

    private Source source;

    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void sendOrderAllocateResponse(AllocateOrderResponse response){
        log.debug("Sending AllocateOrderResponse message for order {} ", response.getOrderDto().getUuid());
        source.output().send(MessageBuilder.withPayload(response).build());
    }
}
