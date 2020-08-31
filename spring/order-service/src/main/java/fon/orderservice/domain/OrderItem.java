package fon.orderservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(OrderItemPK.class)
public class OrderItem {

    @Id
    private Integer serialNumber;

    @Id
    @ManyToOne
    private Order order;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    private String productId;

    private String productCode;

    private Integer quantity;

    private Integer quantityAllocated;

    private BigDecimal subtotal;


}
