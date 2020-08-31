package fon.orderservice.service.product;

import fon.orderservice.web.model.ProductDto;


public interface ProductService {

    ProductDto findProduct(String productCode);

}
