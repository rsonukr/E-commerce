package com.example.ecommerce.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ecommerce.DTO.CartDTO;
import com.example.ecommerce.DTO.ProductDTO;
import com.example.ecommerce.DTO.ProductResponse;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CartService cartService;
	
	public ProductDTO addProduct(Long categoryId,Product product)
	{
		Category category=categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("Category with categoryId "+categoryId+" not found"));
		List<Product> products=category.getProduct();
		boolean isProductNotPresent=true;
		for(Product existingProduct:products)
		{
			if(existingProduct.getName().equals(product.getName())&&existingProduct.getDescription().equals(product.getDescription()))
			{
				isProductNotPresent=false;
				break;
			}
		}
		if(isProductNotPresent) {
			product.setCategory(category);
			Product savedProduct=productRepository.save(product);
			
			ProductDTO productDTO=new ProductDTO();
			productDTO.setProductId(savedProduct.getProductId());
			productDTO.setName(savedProduct.getName());
			productDTO.setDescription(savedProduct.getDescription());
			productDTO.setPrice(savedProduct.getPrice());
			productDTO.setQuantity(savedProduct.getQuantity());
			return productDTO;
		}
		else
			throw new IllegalStateException("Product already exists !!!");
	}
	
	public ProductResponse getProduct(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
	{
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> pageProducts=productRepository.findAll(pageDetails);
		List<Product> products=pageProducts.getContent();
		List<ProductDTO> productDTOs = products.stream()
			       .map(product -> new ProductDTO(product.getProductId(), product.getName(),product.getDescription(),product.getPrice(),product.getQuantity()))
			       .collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
			    
		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());		    
		productResponse.setLastPage(pageProducts.isLast());
			    
		return productResponse;
	}
	
	public ProductResponse searchByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
	{
		Category category=categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("Category with categoryId "+categoryId+" not found"));
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> pageProducts=productRepository.findAll(pageDetails);
		List<Product> products=pageProducts.getContent();
		if(products.size()==0)
			throw new NoSuchElementException(category.getCategoryName()+" category doesn't contain any products !!!");
		List<ProductDTO> productDTOs = products.stream()
			       .map(product -> new ProductDTO(product.getProductId(), product.getName(),product.getDescription(),product.getPrice(),product.getQuantity()))
			       .collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
			    
		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());		    
		productResponse.setLastPage(pageProducts.isLast());
			    
		return productResponse;
	}
	
	public ProductResponse searchProductByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder)
	{
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		Page<Product> pageProducts=productRepository.findByNameLike(keyword, pageDetails);
		List<Product> products=pageProducts.getContent();
		if(products.size()==0)
			throw new NoSuchElementException("Product not found with keyword "+keyword);
		List<ProductDTO> productDTOs = products.stream()
			       .map(product -> new ProductDTO(product.getProductId(), product.getName(),product.getDescription(),product.getPrice(),product.getQuantity()))
			       .collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
			    
		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());		    
		productResponse.setLastPage(pageProducts.isLast());
			    
		return productResponse;
	}
	
	public ProductDTO updateProduct(Long productId, Product product) {
	    Optional<Product> productOptional = productRepository.findById(productId);
	    if (!productOptional.isPresent()) {
	        throw new IllegalArgumentException("Product not found with productId: " + productId);
	    }

	    Product existingProduct = productOptional.get();

	    product.setProductId(productId);
	    product.setCategory(existingProduct.getCategory());

	    Product savedProduct = productRepository.save(product);
	    
	    List<Cart> carts = cartRepository.findCartsByProductId(productId);

		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

		List<ProductDTO> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
		cartDTO.setProducts(products);
		return cartDTO;
		}).collect(Collectors.toList());

		cartDTOs.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(), productId));

		return modelMapper.map(savedProduct, ProductDTO.class);
	}
	
	public String deleteProduct(Long productId) {
	    Optional<Product> productOptional = productRepository.findById(productId);
	    if (!productOptional.isPresent()) {
	        throw new IllegalArgumentException("Product not found with productId: " + productId);
	    }
	    List<Cart> carts = cartRepository.findCartsByProductId(productId);

		carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

	    Product product = productOptional.get();
	    productRepository.delete(product);

	    return "Product with productId: " + productId + " deleted successfully !!!";
	}


	

}
