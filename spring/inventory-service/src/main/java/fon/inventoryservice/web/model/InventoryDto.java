package fon.inventoryservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryDto {

    private UUID uuid;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private UUID productId;
    private String productCode;
    private Integer quantity;
}
