package com.example.ecommerce.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ecommerce.DTO.CategoryDTO;
import com.example.ecommerce.DTO.CategoryResponse;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductService productService;
	
	
	public CategoryDTO createCategory(Category category) {
		Category existingCategory=categoryRepository.findByCategoryName(category.getCategoryName());
		if(existingCategory!=null)
			throw new IllegalStateException("Category with name "+category.getCategoryName()+"already exist!");
		Category savedCategory= categoryRepository.save(category);
		return new CategoryDTO(savedCategory.getCategoryId(),savedCategory.getCategoryName());
	}
	
	public CategoryResponse getCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
	{
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Category> pageCategories=categoryRepository.findAll(pageDetails);
		List<Category> categories=pageCategories.getContent();
		if (categories.isEmpty()) {
		       throw new NoSuchElementException("No category is created till now");
		}

		List<CategoryDTO> categoryDTOs = categories.stream()
		       .map(category -> new CategoryDTO(category.getCategoryId(), category.getCategoryName()))
		       .collect(Collectors.toList());

		CategoryResponse categoryResponse = new CategoryResponse();
		    
		categoryResponse.setContent(categoryDTOs);
		categoryResponse.setPageNumber(pageCategories.getNumber());
		categoryResponse.setPageSize(pageCategories.getSize());
	    categoryResponse.setTotalElements(pageCategories.getTotalElements());
	    categoryResponse.setTotalPages(pageCategories.getTotalPages());		    
	    categoryResponse.setLastPage(pageCategories.isLast());
		    
		return categoryResponse;
	}
	
	public CategoryDTO updateCategory(Category category, Long categoryId){
	    Category savedCategory = categoryRepository.findById(categoryId)
	            .orElseThrow(() -> new NoSuchElementException("Category with categoryId "+categoryId+" not found"));

	    category.setCategoryId(categoryId);

	    savedCategory = categoryRepository.save(category);

	    return new CategoryDTO(savedCategory.getCategoryId(), savedCategory.getCategoryName());
	}
	
	public String deleteCategory(Long categoryId) {
	    Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
	    
	    if (!categoryOptional.isPresent()) {
	        throw new IllegalArgumentException("Category with categoryId " + categoryId + " not found.");
	    }
	    
	    Category category = categoryOptional.get();
	    List<Product> products = category.getProduct();
	    
	    for (Product product : products) {
	    	productService.deleteProduct(product.getProductId());
	    }
	    
	    categoryRepository.delete(category);
	    
	    return "Category with categoryId: " + categoryId + " deleted successfully !!!";
	}

	
	

		
	

}
