package com.ecommerce.project.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;

public interface ProductService {

	public ProductDto createProduct(Long categoryId, ProductDto productDto);

	public ProductResponse getAllProduct();

	public ProductResponse getProductByCategory(Long categoryId);

	public ProductResponse getProductByKeyword(String keyword);

	public ProductDto updateProduct(ProductDto productDto, Long productId);

	public ProductDto deleteProduct(Long productId);

	public ProductDto updateImage(Long productId, MultipartFile image) throws IOException;

}
