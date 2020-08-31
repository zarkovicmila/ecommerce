package fon.cartservice.repository;


import java.util.Map;


public interface CartRedisRepository {

    Map findCartById(String cartId);

    void deleteCartById(String cartId);

    void addItemToCart(String cartId, String productId, String item);

    void deleteItemFromCart(String cartId, String productId);

    void updateItemQuantity(String cartId, String productId, String item);

    String findItem(String cartId, String productId);

}
