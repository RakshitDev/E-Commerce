package com.ecommerce.project.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import com.ecommerce.project.service.FileService;
import com.ecommerce.project.service.ProductService;
@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	FileService fileService;
	@Value("${project.image}")
	String path;
	
	@Override
	public ProductDto createProduct(Long categoryId, ProductDto productDto) {
		//find category based on the id
		Category category = categoryRepository.findById(categoryId)
		.orElseThrow(()->new ResourceNotFoundException("category","categoryId",categoryId));
		
		//check if product already present in with same name or not 
		Optional<Product> existingProduct = productRepository.findByProductNameIgnoreCase(productDto.getProductName());
		if(existingProduct.isPresent()) {
			throw new ApiException("Product Already Present");
		}  
		
		Product product=modelMapper.map(productDto, Product.class);
		product.setCategory(category);
		//set default image and specialPrice
		product.setImage("default.png");
		double specialPrice=product.getPrice()-((product.getDiscount()*0.01)*product.getPrice());
		product.setSpecialPrice(specialPrice);
		Product savedProcut = productRepository.save(product);
		//mapping saved  product to product dto
		return modelMapper.map(savedProcut, ProductDto.class);
	}

	@Override
	public ProductResponse getAllProduct() {
		// get List of product and  and map them 
		//to productDto using modelMapper
		List<ProductDto> productDto = 
				      productRepository.findAll().stream()
		              .map(products->modelMapper.map(products, ProductDto.class))
		             .toList();
		//setting list of productdto to product response
		ProductResponse productResponse=new ProductResponse();
		productResponse.setContent(productDto);
		return productResponse;
	}

	@Override
	public ProductResponse getProductByCategory(Long categoryId) {
		//find category first is exist
		Category category = categoryRepository.findById(categoryId)
		.orElseThrow(()->new ResourceNotFoundException("category","categoryId",categoryId));
		
		//declare  findByCategoryOrderByPriceAsc in repo and get list of productd
		//map them to  productDtos 
		List<ProductDto> productDtos = productRepository.findByCategoryOrderByPriceAsc(category)
		.stream().map(products->modelMapper.map(products, ProductDto.class))
		.toList();
		
		//set product dtos and to product response
		ProductResponse productResponse= new ProductResponse();
		productResponse.setContent(productDtos);
		
		return productResponse;
		
	}

	@Override
	public ProductResponse getProductByKeyword(String keyword) {
		//NameLikeIgoneCase this thing ignore case
		//'%'+keyword+'%' this match the patters for example tra it will find Travel
		List<ProductDto> productDto = productRepository
			    .findByProductNameLikeIgnoreCase("%" + keyword + "%") // Add % manually here
			    .stream()
			    .map(products -> modelMapper.map(products, ProductDto.class))
			    .toList();
		
		ProductResponse productResponse= new ProductResponse();
		productResponse.setContent(productDto);
		
		return productResponse;
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, Long productId) {
		//findById
		Product savedProduct = productRepository.findById(productId)
		.orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
		
		Product product=modelMapper.map(productDto, Product.class);
		//Update the existing one
		savedProduct.setProductName(product.getProductName());
		savedProduct.setDescription(product.getDescription());
		savedProduct.setQuantity(product.getQuantity());
		savedProduct.setPrice(product.getPrice());
		savedProduct.setDiscount(product.getDiscount());
		double specialPrice=product.getPrice()-((product.getDiscount()*0.01)*product.getPrice());
		savedProduct.setSpecialPrice(specialPrice);
		productRepository.save(savedProduct);
		
		//map updated one to the dto and return
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto deleteProduct(Long productId) {
		//findById If Not throw Exception
		Product product = productRepository.findById(productId)
		.orElseThrow(()->new ResourceNotFoundException("product","productId",productId));
		//map it to dto before deleting
		ProductDto productDto = modelMapper.map(product, ProductDto.class);
		//delete the product 
		productRepository.delete(product);
		return productDto;
	}

	@Override
	public ProductDto updateImage(Long productId, MultipartFile image) throws IOException {
		//get Product From Id
		Product product = productRepository.findById(productId)
		.orElseThrow(()->new ResourceNotFoundException("Product","ProductId",productId));
		
		//upload image to the server and get file name of uploaded server
		
		String fileName=fileService.uploadImage(path,image);
		//update a new file name to the product
		product.setImage(fileName);
		
		//save the updated product
		Product updatedProduct = productRepository.save(product);
		//map updated product to product dto and return
		return modelMapper.map(updatedProduct, ProductDto.class);
	}

	

}
