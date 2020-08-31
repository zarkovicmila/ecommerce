package fon.orderservice.service.product;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import fon.orderservice.web.model.ProductDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    public final static String PRODUCT_PATH = "http://product-service/api/v1/product/productCode/";

    private final RestTemplate restTemplate;


    // Hystix parameters: Command properties = {timeout, teshhold, error, sleep}, Bulkhead pattern: threadPoolKey, threadPoolProperties
    @HystrixCommand(
            fallbackMethod = "findProductFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
            })
    @Override
    public ProductDto findProduct(String productCode) {
        return restTemplate.getForObject(PRODUCT_PATH + productCode, ProductDto.class);
    }

    public ProductDto findProductFallback(String productCode){
        log.debug("Calling Hystrix fallback method: findProductFallback");
        return new ProductDto();
    }
}
