package com.ecommerce.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByCategoryOrderByPriceAsc(Category category);

	List<Product> findByProductNameLikeIgnoreCase(String keyword);
	
	Optional<Product> findByProductNameIgnoreCase(String productName);

}
