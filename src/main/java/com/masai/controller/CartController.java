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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cart Controller", description = "APIs for managing the shopping cart")
@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;


    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Add product to cart", description = "Adds a product to the user's cart using the provided token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "CartDTO object containing product details", required = true, content = @Content(schema = @Schema(implementation = CartDTO.class)))
    @PostMapping("/items")
    public ResponseEntity<Cart> addProductToCartHandler(@RequestBody CartDTO cartDto, @RequestHeader("token") String token){
        logger.info("Adding product to cart with token: {}", token);
        Cart cart = cartService.addProductToCart(cartDto, token);
        logger.info("Product added to cart successfully");
        return ResponseEntity.ok(cart);
    }

    @Operation(summary = "Get cart products", description = "Fetches all products in the user's cart using the provided token")
    @GetMapping("/items")
    public ResponseEntity<Cart> getCartProductHandler(@RequestHeader("token") String token){
        logger.info("Fetching cart products for token: {}", token);
        Cart cart = cartService.getCartProduct(token);
        logger.info("Fetched cart products successfully");
        return ResponseEntity.ok(cart);
    }


    @Operation(summary = "Remove product from cart", description = "Removes a specific product from the user's cart using the provided token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "CartDTO object containing product details", required = true, content = @Content(schema = @Schema(implementation = CartDTO.class)))
    @DeleteMapping("/items")
    public ResponseEntity<Cart> removeProductFromCartHandler(@RequestBody CartDTO cartDto, @RequestHeader("token") String token){
        logger.info("Removing product from cart with token: {}", token);
        Cart cart = cartService.removeProductFromCart(cartDto, token);
        logger.info("Product removed from cart successfully");
        return ResponseEntity.ok(cart);
    }


    @Operation(summary = "Clear cart", description = "Clears all products from the user's cart using the provided token")
    @DeleteMapping("/items/clear")
    public ResponseEntity<Void> clearCartHandler(@RequestHeader("token") String token){
        logger.info("Clearing cart for token: {}", token);
        cartService.clearCart(token);
        logger.info("Cart cleared successfully");
        return ResponseEntity.noContent().build();
    }


}
