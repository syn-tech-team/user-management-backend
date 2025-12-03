package com.example.usermanagement.controller;

import com.example.usermanagement.entity.Address;
import com.example.usermanagement.exception.AddressAlreadyExistsException;
import com.example.usermanagement.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Address> createAddress( @PathVariable Long userId, @RequestBody Address address) throws AddressAlreadyExistsException {
        Address createdAddress = addressService.createAddress(userId, address);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long id,
             @RequestBody Address addressDetails) throws AddressAlreadyExistsException {
        Address updateAddress = addressService.updateAddress(id, addressDetails);
        return ResponseEntity.ok(updateAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}