package com.example.usermanagement.service;

import com.example.usermanagement.entity.Address;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.exception.AddressAlreadyExistsException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.repository.AddressRepository;
import com.example.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    }

    @Transactional
    public Address createAddress(Long userId, Address address) throws AddressAlreadyExistsException {
        if (address.getId() != null && addressRepository.existsById(address.getId())) {
            throw new AddressAlreadyExistsException("Address already exists: " + address.getId());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        address.setUser(user);
        return addressRepository.save(address);
    }


    @Transactional
    public Address updateAddress(Long id, Address addressDetails) throws AddressAlreadyExistsException {
        Address address = getAddressById(id);

        // Check if email is being changed and if it already exists
        if (!address.getId().equals(addressDetails.getId()) &&
                addressRepository.existsById(addressDetails.getId())) {
            throw new AddressAlreadyExistsException("Address already exists: " + addressDetails.getId());
        }

        address.setPrimaryAddress(addressDetails.isPrimaryAddress());
        address.setCity(addressDetails.getCity());
        address.setCountry(addressDetails.getCountry());
        address.setStreet(addressDetails.getStreet());
        address.setZipCode(addressDetails.getZipCode());

        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address address = getAddressById(id);
        addressRepository.delete(address);
    }
}