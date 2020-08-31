package fon.inventoryservice.web.model.event;

import fon.inventoryservice.web.model.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllocateOrderResponse {

    private OrderDto orderDto;
    private Boolean allocationError = false;

}
