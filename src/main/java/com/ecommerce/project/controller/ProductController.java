package com.ecommerce.project.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDto> createProduct(@Valid
			@PathVariable("categoryId") Long categoryId,
			@RequestBody ProductDto productDto){
		ProductDto savedProductDto = productService.createProduct(categoryId, productDto);
	return new ResponseEntity<ProductDto>(savedProductDto,HttpStatus.CREATED);
	}
	
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getAllProduct(){
		ProductResponse productResponse= productService.getAllProduct();
		return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
	}
	@GetMapping("/public/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable("categoryId") Long categoryId){
	ProductResponse productResponse=productService.getProductByCategory(categoryId);
	return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
	}
	
	@GetMapping("/public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable("keyword")String keyword){
	ProductResponse productResponse	=productService.getProductByKeyword(keyword);
	return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.FOUND);
	}
	
	@PutMapping("admin/product/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@Valid
			@RequestBody ProductDto productDto,
			@PathVariable("productId") Long productId)
	{
		ProductDto updatedProductDto= productService.updateProduct(productDto,productId);
		return new ResponseEntity<ProductDto>( updatedProductDto,HttpStatus.OK);
	}
	@DeleteMapping("/amin/product/{productId}")
	public ResponseEntity<ProductDto> deleteProduct(@PathVariable("productId") Long productId){
	ProductDto deletedProductDto=	productService.deleteProduct(productId);
	return new ResponseEntity<ProductDto>(deletedProductDto,HttpStatus.OK);
	}
	
	@PutMapping("/product/{productId}/image")
	public ResponseEntity<ProductDto> updateProductImage(
			@PathVariable("productId") Long productId,
			@RequestParam("image") MultipartFile image) throws IOException{
		ProductDto updaDtoImage= productService.updateImage(productId,image);
		return new ResponseEntity<ProductDto>(updaDtoImage,HttpStatus.OK);
	}
	
	
}
