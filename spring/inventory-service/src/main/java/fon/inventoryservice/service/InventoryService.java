package fon.inventoryservice.service;

import fon.inventoryservice.web.model.InventoryDto;
import fon.inventoryservice.web.model.OrderDto;


import java.util.UUID;

public interface InventoryService {

    InventoryDto findProductInventory(UUID productId);
    InventoryDto saveNewInventory(InventoryDto inventoryDto, UUID productId);
    InventoryDto updateProductInventory(InventoryDto inventoryDto);
    OrderDto allocateOrderInventory(OrderDto orderDto);
    void deallocateOrder(OrderDto orderDto);
}
