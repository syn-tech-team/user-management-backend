package com.example.usermanagement.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagement.entity.Address;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.EmailAlreadyExistsException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.AddressRepository;
import com.example.usermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder pwEncoder;
	private final AddressRepository addressRepository;
	
	public User createUser(User user) {
		if(userRepository.findByEmail(user.getEmail()).isPresent())
			throw new EmailAlreadyExistsException("Email already exists!");

		user.setPassword(pwEncoder.encode(user.getPassword()));
		
		if(user.getAddresses() != null) {
			user.getAddresses().forEach(address -> address.setUser(user));
		}
		return userRepository.save(user);
	}
	
	public User updateUser(String id, User user) {
		User foundUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " to update not found."));
		
		if(userRepository.findByEmail(user.getEmail()).isPresent())
			throw new EmailAlreadyExistsException("Updated Email already exists!");
		else
			foundUser.setEmail(user.getEmail());
		
		foundUser.setFirstName(user.getFirstName());
		foundUser.setLastName(user.getLastName());
		foundUser.setImage(user.getImage());
		foundUser.setPassword(pwEncoder.encode(user.getPassword()));
		return userRepository.save(foundUser);
	}
	
	public User addAddressToUser(String userId, Address address) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User to add address to does not exist."));
		address.setUser(user);
		user.getAddresses().add(address);
		return userRepository.save(user);
	}
	
	public User removeAddressFromUser(String userId, String addressId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User to remove address from does not exist."));
		user.getAddresses().removeIf(a -> a.getId().equals(addressId));
		addressRepository.deleteById(addressId);
		return userRepository.save(user);
	}
}