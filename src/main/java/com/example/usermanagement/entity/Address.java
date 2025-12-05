package com.example.usermanagement.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


/*
 * NOT UPDATED ACCORDING TO SQL SCHEMA YET!!! 
 * This is only in the repository so that the List<Address> in User will compile
 */
@Entity
@Data
@Table(name="ADDRESSES")
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    private AddressType type;
    private boolean primaryAddress;

    @JsonIgnore
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = true
    )
    @JoinColumn(name = "USER_ID")
    private User user;
}

