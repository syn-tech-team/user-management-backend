package com.example.usermanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.EmailAlreadyExistsException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder pwEncoder;
	
	public User createUser(User user) {
		if(userRepository.findByEmail(user.getEmail()).isPresent())
			throw new EmailAlreadyExistsException("Updated Email already exists!");

		user.setPassword(pwEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	public User updateUser(long id, User user) {
		User foundUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " to update not found."));
		
		if(userRepository.findByEmail(user.getEmail()).isPresent())
			throw new EmailAlreadyExistsException("Updated Email already exists!");
		else
			foundUser.setEmail(user.getEmail());
		
		foundUser.setName(user.getName());
		foundUser.setPassword(pwEncoder.encode(user.getPassword()));
		return userRepository.save(foundUser);
	}
}