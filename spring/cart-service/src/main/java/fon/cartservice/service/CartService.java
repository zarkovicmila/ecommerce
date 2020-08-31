package fon.cartservice.service;


import fon.cartservice.domain.Item;

import java.util.List;

public interface CartService {

    List<Item> findCartById(String cartId);
    void deleteCartById(String cartId);
    void addItemToCart(String cartId, String productId, Item item);
    void deleteItemFromCart(String cartId, String productId);
    void updateItemQuantity(String cartId, String productId, Integer quantity);

}
