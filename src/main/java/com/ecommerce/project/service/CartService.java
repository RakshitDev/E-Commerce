package com.ecommerce.project.service;

import java.util.List;

import com.ecommerce.project.payload.CartDto;

import jakarta.transaction.Transactional;

public interface CartService {

	CartDto addProductToCart(Long productId, Integer quantity);

	List<CartDto> getAllCarts();

	CartDto getCart(String emailId, Long cartId);
	@Transactional
	CartDto updateProductQuantityInCart(Long productId, int i);
	@Transactional
	public String deletProductFromCart(Long cartId, Long productId);
	
	void updateProductInCarts(Long cartId, Long productId);

}
