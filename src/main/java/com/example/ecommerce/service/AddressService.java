package com.example.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.DTO.AddressDTO;
import com.example.ecommerce.entity.Address;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class AddressService {
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserRepository userRepository;
	
	
	public AddressDTO createAddress(AddressDTO addressDTO) {
	    String country = addressDTO.getCountry();
	    String state = addressDTO.getState();
	    String city = addressDTO.getCity();
	    String pincode = addressDTO.getPincode();
	    String street = addressDTO.getStreet();
	    String buildingName = addressDTO.getBuildingName();

	    Address existindAddress = addressRepository.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
	        country, state, city, pincode, street, buildingName);

	    if (existindAddress != null) {
	        throw new RuntimeException("Address already exists with addressId: " + existindAddress.getAddressId());
	    }

	    Address address = new Address();
	    address.setCountry(country);
	    address.setState(state);
	    address.setCity(city);
	    address.setPincode(pincode);
	    address.setStreet(street);
	    address.setBuildingName(buildingName);

	    Address savedAddress = addressRepository.save(address);

	    return new AddressDTO(savedAddress.getAddressId(), savedAddress.getCountry(), savedAddress.getState(),
	        savedAddress.getCity(), savedAddress.getPincode(), savedAddress.getStreet(),
	        savedAddress.getBuildingName());
	}
	
	public List<AddressDTO> getAddresses() {
	    List<Address> addresses = addressRepository.findAll();

	    List<AddressDTO> addressDTOs = new ArrayList<>();
	    for (Address address : addresses) {
	        AddressDTO addressDTO = new AddressDTO();
	        addressDTO.setAddressId(address.getAddressId());
	        addressDTO.setCountry(address.getCountry());
	        addressDTO.setState(address.getState());
	        addressDTO.setCity(address.getCity());
	        addressDTO.setPincode(address.getPincode());
	        addressDTO.setStreet(address.getStreet());
	        addressDTO.setBuildingName(address.getBuildingName());

	        addressDTOs.add(addressDTO);
	    }

	    return addressDTOs;
	}
	
	public AddressDTO getAddressById(Long addressId) {
	    Optional<Address> optionalAddress = addressRepository.findById(addressId);
	    Address address = optionalAddress.orElseThrow(() ->
	        new RuntimeException("Address not found with addressId: " + addressId));

	    AddressDTO addressDTO = new AddressDTO();
	    addressDTO.setAddressId(address.getAddressId());
	    addressDTO.setCountry(address.getCountry());
	    addressDTO.setState(address.getState());
	    addressDTO.setCity(address.getCity());
	    addressDTO.setPincode(address.getPincode());
	    addressDTO.setStreet(address.getStreet());
	    addressDTO.setBuildingName(address.getBuildingName());

	    return addressDTO;
	}

	
	public AddressDTO updateAddress(Long addressId, Address address) {
	    Address addressFromDB = addressRepository.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
	        address.getCountry(), address.getState(), address.getCity(), address.getPincode(), address.getStreet(),
	        address.getBuildingName());

	    if (addressFromDB == null) {
	        Optional<Address> optionalAddress = addressRepository.findById(addressId);
	        Address existingAddress = optionalAddress.orElseThrow(() ->
	            new RuntimeException("Address not found with addressId: " + addressId));

	        existingAddress.setCountry(address.getCountry());
	        existingAddress.setState(address.getState());
	        existingAddress.setCity(address.getCity());
	        existingAddress.setPincode(address.getPincode());
	        existingAddress.setStreet(address.getStreet());
	        existingAddress.setBuildingName(address.getBuildingName());

	        Address updatedAddress = addressRepository.save(existingAddress);

	        return new AddressDTO(updatedAddress.getAddressId(), updatedAddress.getCountry(),
	            updatedAddress.getState(), updatedAddress.getCity(), updatedAddress.getPincode(),
	            updatedAddress.getStreet(), updatedAddress.getBuildingName());
	    } 
	    else {
	        List<User> users = userRepository.findByAddress(addressId);
	        final Address existingAddress = addressFromDB;

	        users.forEach(user -> user.getAddresses().add(existingAddress));

	        deleteAddress(addressId);

	        return new AddressDTO(existingAddress.getAddressId(), existingAddress.getCountry(),
	            existingAddress.getState(), existingAddress.getCity(), existingAddress.getPincode(),
	            existingAddress.getStreet(), existingAddress.getBuildingName());
	    }
	}
	
	public String deleteAddress(Long addressId) {
	    Optional<Address> optionalAddress = addressRepository.findById(addressId);
	    Address addressFromDB = optionalAddress.orElseThrow(() ->
	        new RuntimeException("Address not found with addressId: " + addressId));

	    List<User> users = userRepository.findByAddress(addressId);
	    users.forEach(user -> user.getAddresses().remove(addressFromDB));
	    users.forEach(userRepository::save);
	    addressRepository.deleteById(addressId);

	    return "Address deleted successfully with addressId: " + addressId;
	}




}
