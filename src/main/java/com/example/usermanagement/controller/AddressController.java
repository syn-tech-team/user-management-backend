package com.example.usermanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagement.entity.Address;
import com.example.usermanagement.service.AddressService;
import com.example.usermanagement.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {
	private final UserService userService;
	private final AddressService addressService;
	
	@PutMapping("/{id}")
	public ResponseEntity<Address> updateAddress(@PathVariable String id, @Valid @RequestBody Address address){
		Address updated = addressService.updateAddress(id, address);
		return ResponseEntity.ok(updated);
	}
}
