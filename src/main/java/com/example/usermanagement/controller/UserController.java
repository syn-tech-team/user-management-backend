package com.example.usermanagement.controller;

import com.example.usermanagement.dto.AddressCreateRequest;
import com.example.usermanagement.dto.AddressResponse;
import com.example.usermanagement.dto.UserCreateRequest;
import com.example.usermanagement.dto.UserResponse;
import com.example.usermanagement.dto.UserUpdateRequest;
import com.example.usermanagement.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest user) {
        UserResponse createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
    	UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok().body(updatedUser);
    }
    
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponse> addAddress(
            @PathVariable String userId,
            @Valid @RequestBody AddressCreateRequest request) {
        AddressResponse response = userService.addAddressToUser(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<Void> removeAddress(
            @PathVariable String userId,
            @PathVariable String addressId) {
        userService.removeAddressFromUser(userId, addressId);
        return ResponseEntity.noContent().build();
    }

}