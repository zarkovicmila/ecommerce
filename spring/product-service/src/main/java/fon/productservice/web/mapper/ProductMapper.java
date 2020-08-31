package fon.productservice.web.mapper;

import fon.productservice.domain.Product;
import fon.productservice.web.model.ProductDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
@DecoratedWith(ProductMapperDecorator.class)
public interface ProductMapper {

    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);
}
