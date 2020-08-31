package fon.orderservice.web.mapper;

import fon.orderservice.domain.OrderItem;
import fon.orderservice.service.product.ProductService;
import fon.orderservice.web.model.OrderItemDto;
import fon.orderservice.web.model.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Optional;

public abstract class OrderItemDecoratorMapper implements OrderItemMapper {

    private ProductService productService;
    private OrderItemMapper orderItemMapper;

    @Autowired
    @Qualifier("delegate")
    public void setOrderItemMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public OrderItemDto orderItemToOrderItemDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = orderItemMapper.orderItemToOrderItemDto(orderItem);

        Optional<ProductDto> productDto = Optional.ofNullable(productService.findProduct(orderItem.getProductCode()));
        productDto.ifPresent(dto -> {
            orderItemDto.setPrice(dto.getPrice());
            orderItemDto.setProductName(dto.getProductName());
            orderItemDto.setType(dto.getType());
        });

        return orderItemDto;
    }
}
