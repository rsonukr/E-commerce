package com.example.ecommerce.DTO;

public class CartItemDTO {
	private Long cartItemId;
	private CartDTO cart;
	private ProductDTO product;
	private Integer quantity;
	private double discount;
	private double productPrice;
	
	public CartItemDTO() {}
	
	public CartItemDTO(Long cartItemId, CartDTO cart, ProductDTO product, Integer quantity, double discount,
			double productPrice) {
		this.cartItemId = cartItemId;
		this.cart = cart;
		this.product = product;
		this.quantity = quantity;
		this.discount = discount;
		this.productPrice = productPrice;
	}

	public Long getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}

	public CartDTO getCart() {
		return cart;
	}

	public void setCart(CartDTO cart) {
		this.cart = cart;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	
	
	

}
