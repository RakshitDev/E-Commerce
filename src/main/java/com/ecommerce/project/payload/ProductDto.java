package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	
	private Long productId;
	private String productName;
	private String image;
	private Integer quantity;
	private double price;
	private double discount;
	private String description;
	private double specialPrice;

}
