package fon.orderservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.io.Serializable;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemPK implements Serializable {

    private Integer serialNumber;

    @ManyToOne
    private Order order;
}
