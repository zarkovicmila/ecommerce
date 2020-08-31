package fon.cartservice.controller;

import fon.cartservice.domain.Item;
import fon.cartservice.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/")
@CrossOrigin
public class CartController {

    private final CartService cartService;


    @PostMapping(value = "cart/{productId}")
    public ResponseEntity addItemToCart(@RequestHeader(value = "uid") String cartId, @PathVariable("productId") String productId, @RequestBody Item item) {
        countSubtotal(item);
        cartService.addItemToCart(cartId, productId, item);
        return new ResponseEntity(item, HttpStatus.CREATED);
    }

    @GetMapping(value = "cart")
    public ResponseEntity getCart(@RequestHeader(value = "uid") String cartId){
        return new ResponseEntity(cartService.findCartById(cartId), HttpStatus.OK);
    }

    @PutMapping(value = "cart/{productId}")
    public ResponseEntity updateItemQuantity(@RequestHeader(value = "uid") String cartId, @PathVariable("productId") String productId, @RequestParam("quantity") Integer quantity){
        cartService.updateItemQuantity(cartId, productId, quantity);
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @DeleteMapping(value = "cart/{productId}")
    public ResponseEntity deleteItemFromCart(@RequestHeader(value = "uid") String cartId, @PathVariable("productId") String productId){
        cartService.deleteItemFromCart(cartId, productId);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "cart")
    public ResponseEntity deleteCart(@RequestHeader(value = "uid") String cartId){
        cartService.deleteCartById(cartId);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    private void countSubtotal(Item item){
        item.setSubtotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
    }
}
