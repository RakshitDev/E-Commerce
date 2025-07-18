package com.ecommerce.project.controller;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.annotation.PutExchange;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCatergory(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORY_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
		CategoryResponse categories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@PostMapping("/public/categories")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		CategoryDto category = categoryService.creatCategory(categoryDto);
		return new ResponseEntity<>(category, HttpStatus.CREATED);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDto> deleteCatogeryById(@PathVariable Long categoryId) {

		CategoryDto deleteCategory = categoryService.deleteCategoryById(categoryId);
		return new ResponseEntity<>(deleteCategory, HttpStatus.OK);

	}

	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId,
			@RequestBody CategoryDto categoryDto) {
		CategoryDto categoryDtos = categoryService.updateCategory(categoryId, categoryDto);
		return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
	}

	

}
