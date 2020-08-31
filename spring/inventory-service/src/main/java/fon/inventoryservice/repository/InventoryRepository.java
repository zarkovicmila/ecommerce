package fon.inventoryservice.repository;

import fon.inventoryservice.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Inventory findFirstByProductId(UUID productId);

    Inventory findFirstByProductCode(String productCode);
}
