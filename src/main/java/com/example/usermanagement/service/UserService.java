package com.example.usermanagement.service;

import java.time.LocalDateTime;

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

		user.setCreatedAt(LocalDateTime.now());
		user.setPassword(pwEncoder.encode(user.getPassword()));
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
		foundUser.setUpdatedAt(LocalDateTime.now());
		foundUser.setPassword(pwEncoder.encode(user.getPassword()));
		return userRepository.save(foundUser);
	}

	public void deleteUser(String userId) {

        // Check if user exists
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Delete will also remove all addresses because of cascade + orphanRemoval
        userRepository.delete(user);
    }
}