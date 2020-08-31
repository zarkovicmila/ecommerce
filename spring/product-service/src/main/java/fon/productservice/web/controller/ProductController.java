package fon.productservice.web.controller;

import fon.productservice.service.ProductService;
import fon.productservice.web.model.ProductDto;
import fon.productservice.web.model.ProductPagedList;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
//@CrossOrigin
@RequestMapping("/api/v1/")
public class ProductController {

    private final ProductService productService;

    @GetMapping(path = "product")
    public ResponseEntity<List<ProductDto>> listProducts(@RequestParam(name = "type", required = false) String type) {
        if (type != null) {
            return new ResponseEntity<>(productService.findByType(type), HttpStatus.OK);
        }
        return new ResponseEntity<>(productService.listProducts(), HttpStatus.OK);
    }

    @GetMapping(path = "products")
    public ProductPagedList listProductsPagination(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 4;
        }
        return productService.listProductPagination(PageRequest.of(pageNumber, pageSize));
    }

    @PostMapping(path = "product")
    public ResponseEntity<ProductDto> saveProduct(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.saveNewProduct(productDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "product/{productId}")
    public ResponseEntity<ProductDto> findProduct(@PathVariable("productId") UUID productId) {
        return new ResponseEntity<>(productService.findProductById(productId), HttpStatus.OK);
    }

    @GetMapping(path = "product/productCode/{productCode}")
    public ResponseEntity<ProductDto> findProduct(@PathVariable("productCode") String productCode) {
        return new ResponseEntity<>(productService.findByProductCode(productCode), HttpStatus.OK);
    }

    @PutMapping(path = "product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") UUID productId, @RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.updateProduct(productId, productDto), HttpStatus.OK);
    }

    @DeleteMapping(path = "product/{productId}")
    public ResponseEntity deleteProduct(@PathVariable("productId") UUID productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
