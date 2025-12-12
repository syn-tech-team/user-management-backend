package com.example.usermanagement.service;

import java.time.LocalDateTime;

import com.example.usermanagement.dto.UserEventType;
import com.example.usermanagement.producer.UserEventProducer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.EmailAlreadyExistsException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder pwEncoder;
	//aileen
	private final UserEventProducer userEventProducer;
	
	public User createUser(User user) {
		if(userRepository.findByEmail(user.getEmail()).isPresent())
			throw new EmailAlreadyExistsException("Updated Email already exists!");

		user.setCreatedAt(LocalDateTime.now());
		user.setPassword(pwEncoder.encode(user.getPassword()));
		user.setActive(true);
		// Save user
		User savedUser = userRepository.save(user);

		// Publish event
		userEventProducer.sendUserEvent(savedUser, UserEventType.USER_CREATED);

		log.info("Created new user with ID: {}", savedUser.getId());
		return savedUser;
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

	//aileen
	@Transactional
	public User activateUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		user.setActive(true);
		User updatedUser = userRepository.save(user);
		userEventProducer.sendUserEvent(updatedUser, UserEventType.USER_ACTIVATED);
		log.info("Activated the  user with ID: {}", updatedUser.getId());
		return updatedUser;
	}

	@Transactional
	public User deactivateUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		user.setActive(false);
		User updatedUser = userRepository.save(user);
		userEventProducer.sendUserEvent(updatedUser, UserEventType.USER_DEACTIVATED);
		log.info("DeActivated the  user with ID: {}", updatedUser.getId());
		return updatedUser;
	}

	public User getUserById(String userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

}