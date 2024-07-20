package com.example.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.DTO.CartDTO;
import com.example.ecommerce.DTO.ProductDTO;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CartItemRepository;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class CartService {
	
		@Autowired
	    private  CartRepository cartRepository;
		@Autowired
	    private  ProductRepository productRepoRepository;
		@Autowired
	    private  CartItemRepository cartItemRepository;
		@Autowired
	    private  ProductRepository productRepository;
	    @Autowired
		private ModelMapper modelMapper;

	    public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) {
	        Cart cart = cartRepository.findById(cartId)
	                .orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found"));

	        Product product = productRepoRepository.findById(productId)
	                .orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));

	        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

	        if (cartItem != null) {
	            throw new RuntimeException("Product " + product.getName() + " already exists in the cart");
	        }

	        if (product.getQuantity() == 0) {
	            throw new RuntimeException(product.getName() + " is not available");
	        }

	        if (product.getQuantity() < quantity) {
	            throw new RuntimeException("Please order " + product.getName()
	                    + " less than or equal to the available quantity (" + product.getQuantity() + ")");
	        }

	        CartItem newCartItem = new CartItem();
	        newCartItem.setProduct(product);
	        newCartItem.setCart(cart);
	        newCartItem.setQuantity(quantity);
	        newCartItem.setProductPrice(product.getPrice());

	        cartItemRepository.save(newCartItem);

	        product.setQuantity(product.getQuantity() - quantity);

	        cart.setTotalPrice(cart.getTotalPrice() + (product.getPrice() * quantity));

	        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<ProductDTO> productDTOs = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

	        cartDTO.setProducts(productDTOs);
	        return cartDTO;
	    }
	    
	    public List<CartDTO> getAllCarts() {
			List<Cart> carts = cartRepository.findAll();
			if (carts.size() == 0) {
				throw new RuntimeException("No cart exists");
			}
			List<CartDTO> cartDTOs = carts.stream().map(cart -> {
				CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

				List<ProductDTO> products = cart.getCartItems().stream()
						.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

				cartDTO.setProducts(products);
				return cartDTO;
			}).collect(Collectors.toList());
			return cartDTOs;
		}
	    
	    public CartDTO getCart(String emailId, Long cartId) {
			Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);

			if (cart == null) {
				throw new RuntimeException("Cart with ID " + cartId + " not found");
			}

			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
			
			List<ProductDTO> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			cartDTO.setProducts(products);

			return cartDTO;
		}
	    
	    public void updateProductInCarts(Long cartId, Long productId) {
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found"));

			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));

			CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

			if (cartItem == null) {
				throw new RuntimeException("Product " + product.getName() + " not available in the cart!!!");
			}

			double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

			cartItem.setProductPrice(product.getPrice());

			cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

			cartItem = cartItemRepository.save(cartItem);
		}
	    
	    public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) {
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found"));
			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new RuntimeException("Product with ID " + productId + " not found"));

			if (product.getQuantity() == 0) {
				throw new RuntimeException(product.getName() + " is not available");
			}

			if (product.getQuantity() < quantity) {
				throw new RuntimeException("Please, make an order of the " + product.getName()
						+ " less than or equal to the quantity " + product.getQuantity() + ".");
			}

			CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

			if (cartItem == null) {
				throw new RuntimeException("Product " + product.getName() + " not available in the cart!!!");
			}

			double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

			product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);

			cartItem.setProductPrice(product.getPrice());
			cartItem.setQuantity(quantity);
			cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * quantity));

			cartItem = cartItemRepository.save(cartItem);

			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

			List<ProductDTO> productDTOs = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			cartDTO.setProducts(productDTOs);

			return cartDTO;
	    }
	    
	    public String deleteProductFromCart(Long cartId, Long productId) {
			Cart cart = cartRepository.findById(cartId)
					.orElseThrow(() -> new RuntimeException("Cart with ID " + cartId + " not found"));

			CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

			if (cartItem == null) {
				throw new RuntimeException("Product with ID " + productId + " not found");
			}

			cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

			Product product = cartItem.getProduct();
			product.setQuantity(product.getQuantity() + cartItem.getQuantity());

			cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

			return "Product " + cartItem.getProduct().getName() + " removed from the cart !!!";
		}



}
