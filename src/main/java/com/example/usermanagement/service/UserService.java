package com.example.usermanagement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.usermanagement.dto.AddressCreateRequest;
import com.example.usermanagement.dto.AddressResponse;
import com.example.usermanagement.dto.AddressUpdateRequest;
import com.example.usermanagement.dto.PasswordUpdateRequest;
import com.example.usermanagement.dto.UserCreateRequest;
import com.example.usermanagement.dto.UserResponse;
import com.example.usermanagement.dto.UserUpdateRequest;
import com.example.usermanagement.entity.Address;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.EmailAlreadyExistsException;
import com.example.usermanagement.exception.InvalidPasswordException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.mapper.AddressMapper;
import com.example.usermanagement.mapper.UserMapper;
import com.example.usermanagement.repository.AddressRepository;
import com.example.usermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final UserMapper userMapper;
	private final AddressMapper addressMapper;
	private final PasswordEncoder pwEncoder;
	
	public UserResponse createUser(UserCreateRequest request) {
		if(userRepository.findByEmail(request.getEmail()).isPresent())
			throw new EmailAlreadyExistsException("Email already exists!");

		
	    User user = userMapper.toEntity(request);
	    return userMapper.toResponse(userRepository.save(user));
	}
	
	public UserResponse updateUser(String userId, UserUpdateRequest request) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
	    if (request.getLastName() != null) user.setLastName(request.getLastName());
	    if (request.getEmail() != null &&
	        userRepository.existsByEmailAndIdNot(request.getEmail(), userId)) {
	        throw new EmailAlreadyExistsException("Email already exists");
	    }
	    if (request.getEmail() != null) user.setEmail(request.getEmail());

	    if (request.getAddresses() != null) {
	        for (AddressUpdateRequest dto : request.getAddresses()) {
	            if (dto.getId() != null) {
	                user.getAddresses().stream()
	                    .filter(a -> a.getId().equals(dto.getId()))
	                    .findFirst()
	                    .ifPresent(a -> {
	                        a.setStreet(dto.getStreet());
	                        a.setCity(dto.getCity());
	                        a.setState(dto.getState());
	                        a.setCountry(dto.getCountry());
	                        a.setZipCode(dto.getZipCode());
	                        a.setType(dto.getType());
	                        a.setPrimaryAddress(dto.isPrimaryAddress());
	                    });
	            } else {
	                Address newAddress = addressMapper.toEntity(dto);
	                newAddress.setUser(user);
	                user.getAddresses().add(newAddress);
	            }
	        }

		    user.getAddresses().removeIf(existing ->
		        existing.getId() != null && request.getAddresses().stream()
		            .noneMatch(dto -> dto.getId() != null && dto.getId().equals(existing.getId()))
		    );
	    }

	    userRepository.save(user);

	    return userMapper.toResponse(user);
	}
	
	public void updatePassword(String userId, PasswordUpdateRequest request) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

	    if (!pwEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
	        throw new InvalidPasswordException("Current password is incorrect");
	    }

	    user.setPassword(pwEncoder.encode(request.getNewPassword()));
	    userRepository.save(user);
	}
	
	
    public AddressResponse addAddressToUser(String userId, AddressCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = addressMapper.toEntity(request);
        address.setUser(user);
        addressRepository.save(address);
        user.getAddresses().add(address);
        userRepository.save(user);
        System.out.println(address.getId());
        return addressMapper.toResponse(address);
    }

    public void removeAddressFromUser(String userId, String addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean removed = user.getAddresses().removeIf(a -> a.getId().equals(addressId));
        if (removed) {
            addressRepository.deleteById(addressId);
        } else {
            throw new ResourceNotFoundException("Address not found for this user");
        }
    }
}