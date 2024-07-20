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

import com.example.ecommerce.DTO.ProductDTO;
import com.example.ecommerce.DTO.ProductResponse;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;

@RestController
@RequestMapping("/admin")
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@GetMapping("/products")
	public ResponseEntity<ProductResponse> getAllProducts(
			@RequestParam(name="pageNumber",defaultValue="0")Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue="10")Integer pageSize,
			@RequestParam(name="sortBy",defaultValue="productId")String sortBy,
			@RequestParam(name="sortOrder",defaultValue="asc")String sortOrder)
	{
		ProductResponse productResponse=productService.getProduct(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}
	
	@PostMapping("/categories/{categoryId}/products")
	public ResponseEntity<ProductDTO> addProductToCategory(@RequestBody Product product,@PathVariable Long categoryId)
	{
		ProductDTO productDTO=productService.addProduct(categoryId, product);
		return new ResponseEntity<>(productDTO,HttpStatus.CREATED);
	}
	
	@GetMapping("/categories/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId,
			@RequestParam(name="pageNumber",defaultValue="0")Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue="10")Integer pageSize,
			@RequestParam(name="sortBy",defaultValue="productId")String sortBy,
			@RequestParam(name="sortOrder",defaultValue="asc")String sortOrder)
	{
		ProductResponse productResponse=productService.searchByCategory(categoryId,pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
	}
	
	@GetMapping("/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,
			@RequestParam(name="pageNumber",defaultValue="0")Integer pageNumber,
			@RequestParam(name="pageSize",defaultValue="10")Integer pageSize,
			@RequestParam(name="sortBy",defaultValue="productId")String sortBy,
			@RequestParam(name="sortOrder",defaultValue="asc")String sortOrder)
	{
		ProductResponse productResponse=productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}
	
	@PutMapping("/{productId}/products")
	public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,@RequestBody Product product)
	{
		ProductDTO savedProduct=productService.updateProduct(productId, product);
		return new ResponseEntity<>(savedProduct,HttpStatus.OK);
	}
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId)
	{
		String status=productService.deleteProduct(productId);
		return new ResponseEntity<>(status,HttpStatus.OK);
	}
	
	

}
