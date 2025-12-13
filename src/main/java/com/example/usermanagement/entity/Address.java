package com.example.usermanagement.entity;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "ADDRESSES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @Column(length = 36)
    private String id;

    @PrePersist
    public void onCreate() {
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Street is required")
    private String street;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "City is required")
    private String city;

    @Column(length = 100)
    private String state;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Country is required")
    private String country;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "Zip code is required")
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType type;

    private boolean primaryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    @JoinColumn(nullable = false)
    private User user;

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

