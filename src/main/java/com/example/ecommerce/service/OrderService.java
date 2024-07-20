package com.example.ecommerce.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ecommerce.DTO.OrderDTO;
import com.example.ecommerce.DTO.OrderItemDTO;
import com.example.ecommerce.DTO.OrderResponse;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod) {

		Optional<Cart> cartOptional = Optional.ofNullable(cartRepository.findCartByEmailAndCartId(emailId, cartId));

	    if (cartOptional.isEmpty()) {
	        throw new RuntimeException("Cart with ID " + cartId + " not found");
	    }

	    Cart cart = cartOptional.get();


		Order order = new Order();

		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());

		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);

		payment = paymentRepository.save(payment);

		order.setPayment(payment);

		Order savedOrder = orderRepository.save(order);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new RuntimeException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);
		}

		orderItems = orderItemRepository.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		
		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
	}
	
	public List<OrderDTO> getOrdersByUser(String emailId) {
		List<Order> orders = orderRepository.findAllByEmail(emailId);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new RuntimeException("No orders placed yet by the user with email: " + emailId);
		}

		return orderDTOs;
	}
	
	public OrderDTO getOrder(String emailId, Long orderId) {

		Order order = orderRepository.findOrderByEmailAndOrderId(emailId, orderId);

		if (order == null) {
			throw new RuntimeException("Order with "+orderId+ " not found!!!");
		}

		return modelMapper.map(order, OrderDTO.class);
	}

	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Order> pageOrders = orderRepository.findAll(pageDetails);

		List<Order> orders = pageOrders.getContent();

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
		
		if (orderDTOs.size() == 0) {
			throw new RuntimeException("No orders placed yet by the users");
		}

		OrderResponse orderResponse = new OrderResponse();
		
		orderResponse.setContent(orderDTOs);
		orderResponse.setPageNumber(pageOrders.getNumber());
		orderResponse.setPageSize(pageOrders.getSize());
		orderResponse.setTotalElements(pageOrders.getTotalElements());
		orderResponse.setTotalPages(pageOrders.getTotalPages());
		orderResponse.setLastPage(pageOrders.isLast());
		
		return orderResponse;
	}

	public OrderDTO updateOrder(String emailId, Long orderId, String orderStatus) {

		Order order = orderRepository.findOrderByEmailAndOrderId(emailId, orderId);

		if (order == null) {
			throw new RuntimeException("Order with "+orderId+ " not found!!!");
		}

		order.setOrderStatus(orderStatus);

		return modelMapper.map(order, OrderDTO.class);
	}
	



}
