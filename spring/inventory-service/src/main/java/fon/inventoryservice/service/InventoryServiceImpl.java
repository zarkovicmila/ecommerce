package fon.inventoryservice.service;

import fon.inventoryservice.domain.Inventory;
import fon.inventoryservice.repository.InventoryRepository;
import fon.inventoryservice.web.mapper.InventoryMapper;
import fon.inventoryservice.web.model.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private RestTemplate restTemplate;


    @Override
    public InventoryDto findProductInventory(UUID productId) {
        return inventoryMapper.inventoryToInventoryDto(inventoryRepository.findFirstByProductId(productId));
    }

    @Transactional
    @Override
    public InventoryDto saveNewInventory(InventoryDto inventoryDto, UUID productId) {

        ResponseEntity<ProductDto> restExchange = restTemplate.exchange(
                "http://product-service/api/v1/product/{productId}",
                HttpMethod.GET,
                null, ProductDto.class, productId);

        if(restExchange == null){
            throw new RuntimeException("Product does not exist with id " + productId);
        }

        inventoryDto.setProductId(productId);

        return inventoryMapper.inventoryToInventoryDto(inventoryRepository.save(inventoryMapper.inventoryDtoToInventory(inventoryDto)));
    }

    @Transactional
    @Override
    public InventoryDto updateProductInventory(InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findById(inventoryDto.getUuid()).orElseThrow(InventoryNotFoundException::new);
        inventory.setQuantity(inventoryDto.getQuantity());

        return inventoryMapper.inventoryToInventoryDto(inventoryRepository.save(inventory));
    }


    @Transactional
    @Override
    public OrderDto allocateOrderInventory(OrderDto orderDto) {
        log.debug("Allocating order {}", orderDto.getUuid().toString());
        BigDecimal totalOrder = BigDecimal.ZERO;

        for (OrderItemDto itemDto : orderDto.getOrderItems()) {
            allocateOrderLineInventory(itemDto);
            totalOrder = totalOrder.add(itemDto.getSubtotal());
        }

        orderDto.setOrderTotal(totalOrder);
        log.debug("Order total {}", orderDto.getOrderTotal());
        return orderDto;
    }

    @Transactional
    @Override
    public void deallocateOrder(OrderDto orderDto) {
        for (OrderItemDto itemDto : orderDto.getOrderItems()) {
            Inventory inventory = inventoryRepository.findFirstByProductCode(itemDto.getProductCode());
            if(inventory != null){
                inventory.setQuantity(inventory.getQuantity() + itemDto.getQuantityAllocated());
                inventoryRepository.save(inventory);
            }
        }
    }


    private void allocateOrderLineInventory(OrderItemDto orderItemDto){
        Inventory inventory = inventoryRepository.findFirstByProductCode(orderItemDto.getProductCode());
        if(inventory != null){

            int allocatedQuantity = 0;

            // full allocation
            if(inventory.getQuantity() >= orderItemDto.getQuantity()){
                allocatedQuantity = orderItemDto.getQuantity();
                orderItemDto.setQuantityAllocated(allocatedQuantity);
                orderItemDto.setSubtotal(BigDecimal.valueOf(allocatedQuantity).multiply(orderItemDto.getPrice()));
                inventory.setQuantity(inventory.getQuantity() - allocatedQuantity);

            } else{
             // partial allocation
                allocatedQuantity = inventory.getQuantity();
                orderItemDto.setQuantityAllocated(allocatedQuantity);
                orderItemDto.setSubtotal(BigDecimal.valueOf(allocatedQuantity).multiply(orderItemDto.getPrice()));
                inventory.setQuantity(0);
            }
                inventoryRepository.save(inventory);

        }
    }
}
