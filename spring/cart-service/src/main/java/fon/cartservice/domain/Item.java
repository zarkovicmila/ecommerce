package fon.cartservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Item {

    private Integer serialNumber;

    private UUID productId;

    private String productCode;

    private String productName;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal subtotal;

    private String url;
}
