package fon.productservice.web.mapper;


import fon.productservice.domain.Product;
import fon.productservice.web.model.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class ProductMapperDecorator implements ProductMapper {

    private ProductMapper mapper;


    @Autowired
    public void setMapper(ProductMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ProductDto productToProductDto(Product product) {
       return mapper.productToProductDto(product);
    }


    @Override
    public Product productDtoToProduct(ProductDto productDto) {
        return mapper.productDtoToProduct(productDto);
    }
}
