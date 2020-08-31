package fon.productservice.service;

import fon.productservice.web.model.ProductDto;
import fon.productservice.web.model.ProductPagedList;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDto> listProducts();

    ProductDto  saveNewProduct(ProductDto productDto);

    ProductDto updateProduct(UUID productId, ProductDto productDto);

    ProductDto findProductById(UUID productId);

    ProductDto findByProductCode(String productCode);

    List<ProductDto> findByType(String type);

    void deleteProduct(UUID productId);

    ProductPagedList listProductPagination(Pageable pageable);
}
