package com.ecommerce.project.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
	private List<CategoryDto>  content;
	private Integer pageNumber;
	private Integer pageSize;
	private Long    totalElements;
	private Integer totalPage;
	private boolean lastPage;
}
