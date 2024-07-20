package com.example.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.ecommerce.DTO.AddressDTO;
import com.example.ecommerce.DTO.CartDTO;
import com.example.ecommerce.DTO.ProductDTO;
import com.example.ecommerce.DTO.UserDTO;
import com.example.ecommerce.DTO.UserResponse;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.CartItem;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.RoleRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CartService cartService;
	
	public UserDTO registerUser(UserDTO userDTO) {
	    try {
	    	User user = modelMapper.map(userDTO, User.class); 
	    	Cart cart = new Cart();
			user.setCart(cart);
	        Role role = roleRepository.findById(102L)
	            .orElseThrow(() -> new RuntimeException("Default role not found"));
	        	user.getRoles().add(role);

	       	String country = userDTO.getAddress().getCountry();
	        String state = userDTO.getAddress().getState();
	        String city = userDTO.getAddress().getCity();
	        String pincode = userDTO.getAddress().getPincode();
	        String street = userDTO.getAddress().getStreet();
	        String buildingName = userDTO.getAddress().getBuildingName();

	        Address address = addressRepository.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
	            country, state, city, pincode, street, buildingName);

	        if (address == null) {
	            address = new Address(country, state, city, pincode, street, buildingName);
	            address = addressRepository.save(address);
	        }

	        user.setAddresses(List.of(address));

	        User registeredUser = userRepository.save(user);
	        cart.setUser(registeredUser);


	        userDTO = modelMapper.map(registeredUser, UserDTO.class);

			userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));


	        return userDTO;
	    } catch (DataIntegrityViolationException e) {
	        throw new RuntimeException("User already exists with emailId: " + userDTO.getEmail());
	    }
	}
	
	public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
	    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
	            : Sort.by(sortBy).descending();
	    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
	    Page<User> pageUsers = userRepository.findAll(pageDetails);
	    List<User> users = pageUsers.getContent();

	    if (users.isEmpty()) {
	        throw new RuntimeException("No User exists !!!");
	    }

	    List<UserDTO> userDTOs = users.stream().map(user -> {
			UserDTO dto = modelMapper.map(user, UserDTO.class);

			if (user.getAddresses().size() != 0) {
				dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
			}

			CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

			List<ProductDTO> products = user.getCart().getCartItems().stream()
					.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

			dto.setCart(cart);

			dto.getCart().setProducts(products);

			return dto;

		}).collect(Collectors.toList());
	    UserResponse userResponse = new UserResponse();
	    userResponse.setContent(userDTOs);
	    userResponse.setPageNumber(pageUsers.getNumber());
	    userResponse.setPageSize(pageUsers.getSize());
	    userResponse.setTotalElements(pageUsers.getTotalElements());
	    userResponse.setTotalPages(pageUsers.getTotalPages());
	    userResponse.setLastPage(pageUsers.isLast());

	    return userResponse;
	}
	
	public UserDTO getUserById(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));

	    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
	    
	    userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

		List<ProductDTO> products = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		userDTO.setCart(cart);

		userDTO.getCart().setProducts(products);

		return userDTO;
	}
	
	public UserDTO updateUser(Long userId, UserDTO userDTO) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));

	    // Encode the password (assuming you have a password field)
	    //String encodedPass = passwordEncoder.encode(userDTO.getPassword());

	    // Update user properties
	    user.setFirstName(userDTO.getFirstName());
	    user.setLastName(userDTO.getLastName());
	    user.setMobileNumber(userDTO.getMobileNumber());
	    user.setEmail(userDTO.getEmail());
	    user.setPassword(userDTO.getPassword());

	    if (userDTO.getAddress() != null) {
	        String country = userDTO.getAddress().getCountry();
	        String state = userDTO.getAddress().getState();
	        String city = userDTO.getAddress().getCity();
	        String pincode = userDTO.getAddress().getPincode();
	        String street = userDTO.getAddress().getStreet();
	        String buildingName = userDTO.getAddress().getBuildingName();

	        Address address = addressRepository.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
	            country, state, city, pincode, street, buildingName);

	        if (address == null) {
	            address = new Address(country, state, city, pincode, street, buildingName);
	            address = addressRepository.save(address);
	            user.setAddresses(List.of(address));
	        }
	    }
	    userDTO = modelMapper.map(user, UserDTO.class);
	    userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));

		CartDTO cart = modelMapper.map(user.getCart(), CartDTO.class);

		List<ProductDTO> products = user.getCart().getCartItems().stream()
				.map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());

		userDTO.setCart(cart);

		userDTO.getCart().setProducts(products);

		return userDTO;
	}
	
	public String deleteUser(Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));
            List<CartItem> cartItems = user.getCart().getCartItems();
    		Long cartId = user.getCart().getCartId();

    		cartItems.forEach(item -> {

    			Long productId = item.getProduct().getProductId();

    			cartService.deleteProductFromCart(cartId, productId);
    		});

    		userRepository.delete(user);

            return "User with userId " + userId + " deleted successfully!!!";
    }
}
