package com.example.ecommerce.controller;

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

import com.example.ecommerce.DTO.CategoryDTO;
import com.example.ecommerce.DTO.CategoryResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.service.CategoryService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping("/create")
	public ResponseEntity<CategoryDTO> addCategory(@RequestBody Category category)
	{
		CategoryDTO savedCategoryDTO=categoryService.createCategory(category);
		return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
	}
	
	@GetMapping("/get")
	public ResponseEntity<CategoryResponse> getCategories(
				@RequestParam (name="pageNumber",defaultValue="0")Integer pageNumber,
				@RequestParam(name="pageSize",defaultValue="10")Integer pageSize,
				@RequestParam(name="sortBy",defaultValue="categoryId")String sortBy,
				@RequestParam(name="sortOrder",defaultValue="asc")String sortOrder)
	{
			CategoryResponse categoryResponse=categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);
			return new ResponseEntity<>(categoryResponse,HttpStatus.OK);		
	}
	
	@PutMapping("/update{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@RequestBody Category category,@PathVariable Long categoryId)
	{
		CategoryDTO categoryDTO=categoryService.updateCategory(category, categoryId);
		return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete{categoryId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long categoryId)
	{
		String status=categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(status,HttpStatus.OK);
	}

}
