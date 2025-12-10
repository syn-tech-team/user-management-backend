package com.example.usermanagement.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.EmailAlreadyExistsException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.UserRepository;
import com.example.usermanagement.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder pwEncoder;

	@InjectMocks
	private UserService userService;

	@Test
	void shouldCreateUserSuccessfully() {

		User user = new User();
		user.setId("1");
		user.setFirstName("Jane");
		user.setLastName("Doe");
		user.setEmail("abc.test.com");
		user.setCreatedAt(LocalDateTime.now());

		user.setPassword("happy_12");
		Mockito.when(pwEncoder.encode("happy_12")).thenReturn("encodedPassword");
		Mockito.when(userRepository.save(user)).thenReturn(user);

		User createdUser = userService.createUser(user);

		assertNotNull(createdUser);
		assertEquals("1", createdUser.getId());
		assertEquals("Jane", createdUser.getFirstName());
		assertEquals("encodedPassword", createdUser.getPassword());
	}

	@Test
	void createUser_ShouldThrowException_WhenEmailAlreadyExists() {

		User user = new User();
		user.setEmail("abc@test.com");
		Mockito.when(userRepository.findByEmail("abc@test.com")).thenReturn(Optional.of(new User()));
		EmailAlreadyExistsException ex = assertThrows(EmailAlreadyExistsException.class,
				() -> userService.createUser(user));
		assertEquals("Updated Email already exists!", ex.getMessage());
	}

	@Test
	void updateUser_ShouldThrowException_ResourceNotFoundException() {

		User user = new User();
		user.setId("1");
		Mockito.when(userRepository.findById("1")).thenReturn(Optional.empty());
		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> userService.updateUser("1", user));
		assertEquals("User with id 1 to update not found.", ex.getMessage());
	}

	@Test
	void shouldUpdateUserSuccessfully() {

		User user = new User();
		user.setId("1");
		user.setFirstName("Jane");
		user.setLastName("Doe");
		user.setEmail("abc.test.com");
		user.setUpdatedAt(LocalDateTime.now());
		user.setPassword("happy_12");
		Mockito.when(pwEncoder.encode("happy_12")).thenReturn("encodedPassword");
		User existing = new User();
		existing.setId("1");
		existing.setPassword("AAABBBCCC");

		Mockito.when(userRepository.findById("1")).thenReturn(Optional.of(existing));
		existing.setFirstName("Jane");
		existing.setLastName("Doe");
		existing.setEmail("abc.test.com");
		existing.setUpdatedAt(LocalDateTime.now());
		existing.setPassword("happy_12");
		Mockito.when(userRepository.save(existing)).thenReturn(existing);

		User updatedUser = userService.updateUser("1", user);

		assertNotNull(updatedUser);
		assertEquals("1", updatedUser.getId());
		assertEquals("Jane", updatedUser.getFirstName());
		assertEquals("encodedPassword", updatedUser.getPassword());
	}
}
