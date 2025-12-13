package com.example.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagement.entity.Address;

public interface AddressRepository extends JpaRepository<Address, String>{

}
