package com.example.usermanagement.mapper;

import org.springframework.stereotype.Component;

import com.example.usermanagement.dto.AddressCreateRequest;
import com.example.usermanagement.dto.AddressResponse;
import com.example.usermanagement.dto.AddressUpdateRequest;
import com.example.usermanagement.entity.Address;

@Component
public class AddressMapper {

	public Address toEntity(AddressCreateRequest request) {
	    Address address = new Address();
	    address.setStreet(request.getStreet());
	    address.setCity(request.getCity());
	    address.setState(request.getState());
	    address.setCountry(request.getCountry());
	    address.setZipCode(request.getZipCode());
	    address.setType(request.getType());
	    address.setPrimaryAddress(request.isPrimaryAddress());
	    return address;
	}
	
    public Address toEntity(AddressUpdateRequest request) {
        Address address = new Address();
        address.setId(request.getId());
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setZipCode(request.getZipCode());
        address.setType(request.getType());
        address.setPrimaryAddress(request.isPrimaryAddress());
        return address;
    }

    public AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .zipCode(address.getZipCode())
                .type(address.getType())
                .primaryAddress(address.isPrimaryAddress())
                .build();
    }
}
