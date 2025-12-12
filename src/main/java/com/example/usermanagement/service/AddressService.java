package com.example.usermanagement.service;

import org.springframework.stereotype.Service;

import com.example.usermanagement.entity.Address;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
	private final AddressRepository addressRepository;
	
	
	public Address updateAddress(String id, Address address) {
		Address foundA = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address to update does not exist."));
		foundA.setCity(address.getCity());
		foundA.setCountry(address.getCountry());
		foundA.setPrimaryAddress(address.isPrimaryAddress() ? true : false);
		foundA.setState(address.getState());
		foundA.setStreet(address.getStreet());
		foundA.setType(address.getType());
		foundA.setZipCode(address.getZipCode());
		
		return addressRepository.save(foundA);
	}
	
}
