package com.masai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.masai.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

}
