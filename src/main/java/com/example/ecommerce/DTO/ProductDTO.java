package com.example.ecommerce.DTO;

public class ProductDTO {
	
	private Long productId;
	private String name;
	private String description;
	private double price;
	private Integer quantity;
	
	public ProductDTO() {
		
	}
	public ProductDTO(Long productId, String name, String description, double price, Integer quantity) {
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	

}
