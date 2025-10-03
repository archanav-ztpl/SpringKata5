package com.masai.service.interfaces;

import com.masai.exception.ProductNotFound;
import com.masai.exception.CartItemNotFound;
import com.masai.model.Cart;
import com.masai.dto.CartDTO;


public interface CartService {
	
	public Cart addProductToCart(CartDTO cart, String token) throws CartItemNotFound;
	public Cart getCartProduct(String token);
	public Cart removeProductFromCart(CartDTO cartDto,String token) throws ProductNotFound;
//	public Cart changeQuantity(Product product,Customer customer,Integer quantity);
	
	public Cart clearCart(String token);
	
}
