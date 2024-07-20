package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.DTO.OrderDTO;
import com.example.ecommerce.DTO.OrderResponse;
import com.example.ecommerce.service.OrderService;

@RestController
@RequestMapping("/")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/users/{emailId}/carts/{cartId}/payments/{paymentMethod}/order")
	public ResponseEntity<OrderDTO> orderProducts(@PathVariable String emailId, @PathVariable Long cartId, @PathVariable String paymentMethod) {
		OrderDTO order = orderService.placeOrder(emailId, cartId, paymentMethod);
		
		return new ResponseEntity<OrderDTO>(order, HttpStatus.CREATED);
	}

	@GetMapping("/admin/orders")
	public ResponseEntity<OrderResponse> getAllOrders(
			@RequestParam(name = "pageNumber", defaultValue ="default=0", required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "default=10", required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = "default=totalAmount", required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = "default=asc", required = false) String sortOrder) {
		
		OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
	}
	
	@GetMapping("/users/{emailId}/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable String emailId) {
		List<OrderDTO> orders = orderService.getOrdersByUser(emailId);
		
		return new ResponseEntity<List<OrderDTO>>(orders, HttpStatus.FOUND);
	}
	
	@GetMapping("/users/{emailId}/orders/{orderId}")
	public ResponseEntity<OrderDTO> getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) {
		OrderDTO order = orderService.getOrder(emailId, orderId);
		
		return new ResponseEntity<OrderDTO>(order, HttpStatus.FOUND);
	}
	
	@PutMapping("/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
	public ResponseEntity<OrderDTO> updateOrderByUser(@PathVariable String emailId, @PathVariable Long orderId, @PathVariable String orderStatus) {
		OrderDTO order = orderService.updateOrder(emailId, orderId, orderStatus);
		
		return new ResponseEntity<OrderDTO>(order, HttpStatus.OK);
	}
}
