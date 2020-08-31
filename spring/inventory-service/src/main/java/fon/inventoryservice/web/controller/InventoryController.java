package fon.inventoryservice.web.controller;

import fon.inventoryservice.service.InventoryService;
import fon.inventoryservice.web.model.InventoryDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/api/v1/")
@RestController
public class InventoryController {

    private final InventoryService inventoryService;


    @GetMapping("product/{productId}/inventory")
    public ResponseEntity<InventoryDto> listProductInventory(@PathVariable("productId") UUID productId){
        return new ResponseEntity<>(inventoryService.findProductInventory(productId), HttpStatus.OK);
    }

    @PostMapping("product/{productId}/inventory")
    public ResponseEntity saveNewInventory(@PathVariable("productId") UUID productId, @RequestBody InventoryDto inventoryDto){
        if(inventoryService.findProductInventory(productId) != null){
            return new ResponseEntity<>("Inventory for this product already exists.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(inventoryService.saveNewInventory(inventoryDto, productId), HttpStatus.CREATED);
    }

    @PutMapping("product/{productId}/inventory")
    public ResponseEntity<InventoryDto> updateProductInventory(@PathVariable("productId") UUID productId, @RequestBody InventoryDto inventoryDto){
        return new ResponseEntity<>(inventoryService.updateProductInventory(inventoryDto), HttpStatus.CREATED);
    }
}
