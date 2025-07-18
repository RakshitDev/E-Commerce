package com.ecommerce.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.project.model.Cart;
import com.ecommerce.project.payload.CartDto;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class CartController {
	@Autowired
	CartService cartService;

	@Autowired
	AuthUtil authUtil;

	@Autowired
	CartRepository cartRepository;

	@PostMapping("/carts/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDto> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
		CartDto cartDto = cartService.addProductToCart(productId, quantity);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.CREATED);
	}

	@GetMapping("/carts")
	public ResponseEntity<List<CartDto>> getAllCarts() {
		List<CartDto> cartDto = cartService.getAllCarts();
		return new ResponseEntity<List<CartDto>>(cartDto, HttpStatus.OK);
	}

	@GetMapping("/carts/users/cart")
	public ResponseEntity<CartDto> getCartById() {
		String emailId = authUtil.loggedInEmail();
		Cart cart = cartRepository.findCartByEmail(emailId);
		Long cartId = cart.getCartId();
		CartDto cartDto = cartService.getCart(emailId, cartId);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}

	@PutMapping("/cart/products/{productId}/quantity/{operation}")
	public ResponseEntity<CartDto> updateProductQuantityInCart(@PathVariable Long productId,
			@PathVariable String operation) {
		CartDto cartDto = cartService.updateProductQuantityInCart(productId,
				operation.equalsIgnoreCase("delete") ? -1 : 1);
		return new ResponseEntity<CartDto>(cartDto, HttpStatus.OK);
	}
	@DeleteMapping("/carts/{cartId}/product/{productId}")
	public ResponseEntity<String> deletProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
		String message = cartService.deletProductFromCart(cartId, productId);
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}

}
