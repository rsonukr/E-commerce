package com.example.ecommerce.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.DTO.AddressDTO;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.service.AddressService;

@RestController
@RequestMapping("/admin")
public class AddressController {
	@Autowired
	private AddressService addressService;
	
	@PostMapping("/addresses")
	public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO)
	{
		AddressDTO savedAddress=addressService.createAddress(addressDTO);
		return new ResponseEntity<>(savedAddress,HttpStatus.CREATED);
	}
	
	@GetMapping("/addresses")
	public ResponseEntity<List<AddressDTO>> getAddresses()
	{
		List<AddressDTO> existingAddress=addressService.getAddresses();
		return new ResponseEntity<>(existingAddress,HttpStatus.FOUND);
	}
	
	@GetMapping("/addresses/{addressId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable Long addressID)
	{
		AddressDTO existingAddressDTO=addressService.getAddressById(addressID);
		return new ResponseEntity<>(existingAddressDTO,HttpStatus.FOUND);
	}
	
	@PutMapping("/addresses/{addressId}")
	public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,@RequestBody Address adrress)
	{
		AddressDTO savedDTO=addressService.updateAddress(addressId, adrress);
		return new ResponseEntity<>(savedDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/addresses/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long addressId)
	{
		String status=addressService.deleteAddress(addressId);
		return new ResponseEntity<>(status,HttpStatus.OK);
	}
	

}
