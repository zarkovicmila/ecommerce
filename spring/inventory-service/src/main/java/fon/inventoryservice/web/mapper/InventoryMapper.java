package fon.inventoryservice.web.mapper;


import fon.inventoryservice.domain.Inventory;
import fon.inventoryservice.web.model.InventoryDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface InventoryMapper {

    InventoryDto inventoryToInventoryDto(Inventory inventory);

    Inventory inventoryDtoToInventory(InventoryDto inventoryDto);
}
