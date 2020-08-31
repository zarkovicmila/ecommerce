package fon.productservice.service;

import fon.productservice.domain.Product;
import fon.productservice.reposirtory.ProductRepository;
import fon.productservice.web.mapper.ProductMapper;
import fon.productservice.web.model.ProductDto;
import fon.productservice.web.model.ProductNotFoundException;
import fon.productservice.web.model.ProductPagedList;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

   private final ProductRepository productRepository;
   private final ProductMapper productMapper;

    @Override
    public List<ProductDto> listProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = productList.stream().map(productMapper::productToProductDto).collect(Collectors.toList());
        return productDtoList;
    }

    @Transactional
    @Override
    public ProductDto saveNewProduct(ProductDto productDto) {
       return productMapper.productToProductDto(productRepository.save(productMapper.productDtoToProduct(productDto)));
    }

    @Transactional
    @Override
    public ProductDto updateProduct(UUID productId, ProductDto productDto) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setType(productDto.getType());
        product.setProductName(productDto.getProductName());

        return productMapper.productToProductDto(productRepository.save(product));
    }

    @Override
    public ProductDto findProductById(UUID productId) {
        log.debug("Finding product with id {}", productId);
        return  productMapper.productToProductDto(productRepository.findById(productId).orElseThrow(ProductNotFoundException::new));
    }

    @Override
    public ProductDto findByProductCode(String productCode) {
        return productMapper.productToProductDto(productRepository.findByProductCode(productCode));
    }

    @Override
    public List<ProductDto> findByType(String type) {
        List<Product> productList = productRepository.findByType(type);
        List<ProductDto> productDtoList = productList.stream().map(productMapper::productToProductDto).collect(Collectors.toList());
        return productDtoList;
    }

    @Transactional
    @Override
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }

    @Override
    public ProductPagedList listProductPagination(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return new ProductPagedList(productPage
                .stream()
                .map(productMapper::productToProductDto)
                .collect(Collectors.toList()), PageRequest.of(productPage.getPageable().getPageNumber(), productPage.getPageable().getPageSize()), productPage.getTotalElements());
    }
}
