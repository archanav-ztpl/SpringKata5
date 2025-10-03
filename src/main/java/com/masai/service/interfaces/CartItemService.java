package com.masai.service.interfaces;

import com.masai.dto.CartDTO;
import com.masai.model.CartItem;

public interface CartItemService {
	
	public CartItem createItemforCart(CartDTO cartdto);
	
}
