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

import com.example.ecommerce.DTO.UserDTO;
import com.example.ecommerce.DTO.UserResponse;
import com.example.ecommerce.service.UserService;

@RestController
@RequestMapping("/")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/users")
	public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO)
	{
		UserDTO savedUserDTO=userService.registerUser(userDTO);
		return new ResponseEntity<>(savedUserDTO,HttpStatus.CREATED);
	}
	
	@GetMapping("/users")
	public ResponseEntity<UserResponse> getUsers(
			@RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = "userId", required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {
		
		UserResponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.FOUND);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
		UserDTO user = userService.getUserById(userId);
		
		return new ResponseEntity<UserDTO>(user, HttpStatus.FOUND);
	}
	
	@PutMapping("/users/{userId}")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
		UserDTO updatedUser = userService.updateUser(userId, userDTO);
		
		return new ResponseEntity<UserDTO>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		String status = userService.deleteUser(userId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);

}
}
