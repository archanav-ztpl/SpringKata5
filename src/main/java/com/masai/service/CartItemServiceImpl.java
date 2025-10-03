package com.masai.service;

import com.masai.service.interfaces.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.ProductNotFoundException;
import com.masai.dto.CartDTO;
import com.masai.model.CartItem;
import com.masai.model.Product;
import com.masai.model.ProductStatus;
import com.masai.repository.ProductRepository;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
    ProductRepository productRepository;

	@Override
	public CartItem createItemForCart(CartDTO cartDto) {

		Product existingProduct = productRepository.findById(cartDto.getProductId()).orElseThrow( () -> new ProductNotFoundException("Product Not found"));

		if(existingProduct.getStatus().equals(ProductStatus.OUTOFSTOCK) || existingProduct.getQuantity() == 0) {
			throw new ProductNotFoundException("Product OUT OF STOCK");
		}
		
		CartItem newItem = new CartItem();
		
		newItem.setCartItemQuantity(1);
		
		newItem.setCartProduct(existingProduct);
		
		return newItem;
	}
	
//	@Override
//	public CartItem addItemToCart(CartDTO cartDto) {
//
//		// TODO Auto-generated method stub
//		
////		Product existingProduct = productRepository.findById(cartDto.getProductId()).orElseThrow( () -> new ProductException("Product Not found"));
//
//		Optional<Product> opt = productRepository.findById(cartDto.getProductId());
//
//		if(opt.isEmpty())
//			throw new ProductNotFoundException("Product not found");
//		
//		Product existingProduct = opt.get();
//		
//		CartItem newItem = new CartItem();
//		
//		newItem.setCartProduct(existingProduct);
//		
//		newItem.setCartItemQuantity(1);
//		
//		return newItem;
//	}

}
