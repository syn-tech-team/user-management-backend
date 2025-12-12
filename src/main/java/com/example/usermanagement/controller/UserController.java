package com.example.usermanagement.controller;

import com.example.usermanagement.entity.Address;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable String id,
             @Valid @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }
    
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<User> addAddress(
            @PathVariable String userId,
            @Valid @RequestBody Address address) {
        User updatedUser = userService.addAddressToUser(userId, address);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<User> removeAddress(
            @PathVariable String userId,
            @PathVariable String addressId) {
        User updatedUser = userService.removeAddressFromUser(userId, addressId);
        return ResponseEntity.ok(updatedUser);
    }

}