package com.ecommerce.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.project.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	 public  Optional<Category> findByCategoryName(String categoryName);
	

}
