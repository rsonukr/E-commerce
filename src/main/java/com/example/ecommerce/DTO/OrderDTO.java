package com.example.ecommerce.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
	private Long orderId;
	private String email;
	private List<OrderItemDTO> orderItems = new ArrayList<>();
	private LocalDate orderDate;
	//private PaymentDTO payment;
	private Double totalAmount;
	private String orderStatus;
	
	public OrderDTO() {}
	
	public OrderDTO(Long orderId, String email, List<OrderItemDTO> orderItems, LocalDate orderDate, Double totalAmount,
			String orderStatus) {
		this.orderId = orderId;
		this.email = email;
		this.orderItems = orderItems;
		this.orderDate = orderDate;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<OrderItemDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemDTO> orderItems) {
		this.orderItems = orderItems;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
	

}
