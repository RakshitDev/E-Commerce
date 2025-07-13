package com.ecommerce.project.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	public CategoryRepository categoryRepository;
	
	@Autowired
	public ModelMapper modelMapper;

	@Override
	public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy, String sortOrder) {
		//object sorting based on the request
		Sort sortByAndOrder=
				sortOrder.equalsIgnoreCase("asc")
				?Sort.by(sortBy).ascending()
				:Sort.by(sortBy).descending();
		// Create pageable object
		PageRequest pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
		  // Fetch paginated data
		Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
		// Handle empty data	
		if (categoryPage.isEmpty())
			throw new ApiException("Category not created till Now ");
		 // Convert entities to DTOs
		List<CategoryDto> categoryDtos = categoryPage.stream()
		.map(category->modelMapper.map(category, CategoryDto.class) )
		.toList();
		   // Prepare response DTO
		CategoryResponse categoryResponse= new CategoryResponse();
		categoryResponse.setContent(categoryDtos);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPage(categoryPage.getTotalPages());
		categoryResponse.setLastPage(categoryPage.isLast());
		return categoryResponse;
	}

	@Override
	public CategoryDto creatCategory(CategoryDto categoryDto) {
//		Category category = modelMapper.map(categoryDto, Category.class);
//		 Optional<Category> byCategoryName = categoryRepository.findByCategoryName(category.getCategoryName());
//
//		if (byCategoryName.isPresent()) {
//			throw new ApiException("Category " + categoryDto.getCategoryName() + " already exists !!!");
//		}
//		Category savedCategory = categoryRepository.save(category);
//		CategoryDto categoryDtos = modelMapper.map(savedCategory, CategoryDto.class);
//		return categoryDtos;
		
		Category category = modelMapper.map(categoryDto, Category.class);
		categoryRepository.findByCategoryName(categoryDto.getCategoryName()).ifPresent(c -> {
			throw new ApiException("Category " + categoryDto.getCategoryName() + " already exists !!!");
		});
		
		Category savedCategory = categoryRepository.save(category);
		CategoryDto savedCategoryDto = modelMapper.map(savedCategory, CategoryDto.class);
		return savedCategoryDto ;
	}

	@Override
	public CategoryDto deleteCategoryById(Long categoryId) {
//		 Category category = categoryRepository.findById(categoryId)
//		 .orElseThrow(()->new ResourceNotFoundException("CategoryName", "CategoryId", categoryId));
//		
//		
//		categoryRepository.delete(category);
//		return modelMapper.map(category, CategoryDto.class);

		return categoryRepository.findById(categoryId).map((savedCategory) -> {
			CategoryDto deletedCategoryDto = modelMapper.map(savedCategory, CategoryDto.class);
			categoryRepository.delete(savedCategory);
		
			return deletedCategoryDto;
		}).orElseThrow(() -> new ResourceNotFoundException("CategoryName", "CategoryId", categoryId));

	}

	@Override
	public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
//		Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
//		
//		Category savedCategory = savedCategoryOptional.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not found by Id "));
//		// Update the field(s)
//		savedCategory.setCategoryName(categoryDto.getCategoryName());
//	    // Save to DB
//		savedCategory = categoryRepository.save(savedCategory);
//		// Convert back to DTO
//		return modelMapper.map(savedCategory, CategoryDto.class);
//		
		return categoryRepository.findById(categoryId).map(savedCategory -> {
			savedCategory.setCategoryName(categoryDto.getCategoryName());
			
			 Category updatedCategory = categoryRepository.save(savedCategory);
			 return modelMapper.map(updatedCategory,CategoryDto.class);
		}).orElseThrow(() -> new ResourceNotFoundException("CategoryName", "CategoryId", categoryId));

	}

}
