package fon.cartservice.repository;

import fon.cartservice.domain.Item;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public class CartRedisRepositoryImpl implements CartRedisRepository {

    private final RedisTemplate<String, Item> redisTemplate;

    private final HashOperations hashOperations;

    public CartRedisRepositoryImpl(RedisTemplate<String, Item> redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Map findCartById(String cartId) {
        Map map = hashOperations.entries(cartId);
        return map;
    }

    @Override
    public void deleteCartById(String cartId) {
        redisTemplate.delete(cartId);
    }

    @Override
    public void addItemToCart(String cartId, String itemId, String item) {
        hashOperations.putIfAbsent(cartId, itemId, item);
    }

    @Override
    public void deleteItemFromCart(String cartId, String itemId) {
        hashOperations.delete(cartId,itemId);
    }

    @Override
    public void updateItemQuantity(String cartId, String itemId, String item) {
        hashOperations.put(cartId, itemId, item);
    }

    @Override
    public String findItem(String cartId, String productId) {
        return (String) hashOperations.get(cartId, productId);
    }

}
