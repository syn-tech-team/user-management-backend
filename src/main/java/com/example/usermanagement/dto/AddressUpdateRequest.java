package com.example.usermanagement.dto;

import com.example.usermanagement.entity.AddressType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateRequest {
    private String id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private AddressType type;
    private boolean primaryAddress;
}
