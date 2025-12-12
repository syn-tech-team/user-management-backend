package com.example.usermanagement.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserEvent(
        @NotBlank String userId,
        @Email String email,
        @NotBlank String fullName,
        @NotBlank UserEventType eventType,
        LocalDateTime timestamp ){

};
