package com.masai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masai.model.Cart;
import com.masai.dto.CartDTO;
import com.masai.service.interfaces.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;


    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Cart> addProductToCartHandler(@RequestBody CartDTO cartDto ,@RequestHeader("token")String token){

        Cart cart = cartService.addProductToCart(cartDto, token);
        return ResponseEntity.ok(cart);
    }

//
    @GetMapping
    public ResponseEntity<Cart> getCartProductHandler(@RequestHeader("token")String token){
        return ResponseEntity.ok(cartService.getCartProduct(token));
    }


    @DeleteMapping
    public ResponseEntity<Cart> removeProductFromCartHandler(@RequestBody CartDTO cartDto ,@RequestHeader("token")String token){

        Cart cart = cartService.removeProductFromCart(cartDto, token);
        return ResponseEntity.ok(cart);
    }


    @DeleteMapping(value = "/clear")
    public ResponseEntity<Void> clearCartHandler(@RequestHeader("token") String token){
        cartService.clearCart(token);
        return ResponseEntity.noContent().build();
    }


}
