package fon.cartservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fon.cartservice.domain.Item;
import fon.cartservice.repository.CartRedisRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRedisRepository cartRedisRepository;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    @Override
    public List<Item> findCartById(String cartId) {

        Map cartMap = cartRedisRepository.findCartById(cartId);

        if (cartMap == null) {
            return null;
        }

        List<Item> items = new ArrayList<>();
        cartMap.forEach((k, v) -> {
            try {
                Item item = objectMapper.readValue((String) v, Item.class);
                item.setSerialNumber(items.size() + 1);
                items.add(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return items;
    }

    @Override
    public void deleteCartById(String cartId) {
        cartRedisRepository.deleteCartById(cartId);
    }

    @Override
    public void addItemToCart(String cartId, String productId, Item item) {

        try {
            /*
            ResponseEntity<Item> restExchange = restTemplate.exchange(
                    "http://product-service/api/v1/product/{productId}",
                    HttpMethod.GET,
                    null, Item.class, productId);

*/
            cartRedisRepository.addItemToCart(cartId, productId, objectMapper.writeValueAsString(item));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteItemFromCart(String cartId, String productId) {
        cartRedisRepository.deleteItemFromCart(cartId, productId);
    }

    @Override
    public void updateItemQuantity(String cartId, String productId, Integer quantity) {
        try {
            Item item = objectMapper.readValue(cartRedisRepository.findItem(cartId, productId), Item.class);
            item.setQuantity(quantity);
            item.setSubtotal(item.getPrice().multiply(new BigDecimal(quantity)));
            cartRedisRepository.updateItemQuantity(cartId, productId, objectMapper.writeValueAsString(item));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
