package com.ecommerce.project.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDto;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.repositories.CartItemRepository;
import com.ecommerce.project.repositories.CartRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.util.AuthUtil;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartItemRepository cartItemRepository;
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AuthUtil authUtil;

	@Override
	public CartDto addProductToCart(Long productId, Integer quantity) {
		// find cart by logged in user if not found create one
		Cart cart = createCart();
		// find product based on product id if not throw exception
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product ", "productId", productId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
		if (cartItem != null) {
			throw new ApiException("Product " + product.getProductName() + "already exists!!");
		}
		if (product.getQuantity() == 0) {
			throw new ApiException("product" + product.getProductName() + " does not existd");
		}
		if (product.getQuantity() < quantity) {
			throw new ApiException("Plese do add" + product.getProductName() + " quantity less then or equal to "
					+ product.getQuantity());
		}
		CartItem newCartItem = new CartItem();
		newCartItem.setProduct(product);
		newCartItem.setCart(cart);
		newCartItem.setDiscount(product.getDiscount());
		newCartItem.setQuantity(quantity);
		newCartItem.setProductPrice(product.getSpecialPrice());

		// Save the cart item to persist it in DB
		cartItemRepository.save(newCartItem);

		product.setQuantity(product.getQuantity());
		cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice()) * quantity);
		cartRepository.save(cart);

		CartDto cartDto = modelMapper.map(cart, CartDto.class);

		List<CartItem> cartItems = cart.getCartItems();
		Stream<ProductDto> productDto = cartItems.stream().map(item -> {
			ProductDto map = modelMapper.map(item.getProduct(), ProductDto.class);
			map.setQuantity(item.getQuantity());
			return map;
		});
		cartDto.setProducts(productDto.toList());

		return cartDto;
	}

	// this is the helper method help in finding cart form logged in email if not
	// found it will create the cart from the email
	private Cart createCart() {
		Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
		if (userCart != null) {
			return userCart;
		}
		Cart cart = new Cart();
		cart.setTotalPrice(0.0);
		cart.setUser(authUtil.loggedInUser());
		Cart newCart = cartRepository.save(cart);
		return newCart;

	}

	@Override
	public List<CartDto> getAllCarts() {
		List<Cart> carts = cartRepository.findAll();
		if (carts.size() == 0) {
			throw new ApiException("Cart is Empty");
		}
		List<CartDto> cartDtos = carts.stream().map(cart -> {
			CartDto cartDto = modelMapper.map(cart, CartDto.class);
			List<ProductDto> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDto.class)).collect(Collectors.toList());
			cartDto.setProducts(products);
			return cartDto;
		}).collect(Collectors.toList());
		return cartDtos;
	}

	@Override
	public CartDto getCart(String emailId, Long cartId) {
		Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}
		cart.getCartItems().forEach(c -> c.getProduct().setQuantity(c.getQuantity()));
		CartDto cartDto = modelMapper.map(cart, CartDto.class);
		List<ProductDto> products = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDto.class)).toList();
		cartDto.setProducts(products);
		return cartDto;
	}

	@Override
	@Transactional
	public CartDto updateProductQuantityInCart(Long productId, int quantity) {
		// we need cartId from the the loggedIn user
		// so first get logged in user and then get cart from email you got
		String email = authUtil.loggedInEmail();
		Cart userCart = cartRepository.findCartByEmail(email);
		Long cartId = userCart.getCartId();
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));
		// some validation on the product
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product ", "productId", productId));

		if (product.getQuantity() == 0) {
			throw new ApiException("product" + product.getProductName() + " does not existd");
		}
		if (product.getQuantity() < quantity) {
			throw new ApiException("Plese do add" + product.getProductName() + " quantity less then or equal to "
					+ product.getQuantity());
		}
		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
		if (cartItem == null) {
			throw new ApiException("product " + product.getProductName() + " does not exists!!");
		}
		int newQuantity = cartItem.getQuantity() + quantity;
		if (newQuantity < 0) {
			throw new ApiException("cart quantity cannot be negative");
		}
		cartItem.setProductPrice(product.getSpecialPrice());
		cartItem.setQuantity(cartItem.getQuantity() + quantity);
		cartItem.setDiscount(product.getDiscount());
		cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
		cartRepository.save(cart);
		CartItem updatedItem = cartItemRepository.save(cartItem);
		// if quantity is zero in the cart then delete the cartItem
		if (updatedItem.getQuantity() == 0) {
			cartItemRepository.deleteById(updatedItem.getCartItemId());
		}
		CartDto cartDto = modelMapper.map(cart, CartDto.class);
		List<CartItem> cartItems = cart.getCartItems();

		Stream<ProductDto> productStream = cartItems.stream().map(item -> {
			ProductDto prd = modelMapper.map(item.getProduct(), ProductDto.class);
			prd.setQuantity(item.getQuantity());
			return prd;
		});
		cartDto.setProducts(productStream.toList());
		return cartDto;
	}

	@Override
	@Transactional
	public String deletProductFromCart(Long cartId, Long productId) {

		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}

		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

		cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

		return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
	}
	
	@Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if (cartItem == null) {
            throw new ApiException("Product " + product.getProductName() + " not available in the cart!!!");
        }

        double cartPrice = cart.getTotalPrice()
                - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice
                + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);
    }


}
