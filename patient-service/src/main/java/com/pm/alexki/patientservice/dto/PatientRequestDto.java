package com.pm.alexki.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PatientRequestDto(
        @NotBlank(message = "Name is required")
        @Size(min = 10, message = "Name should be longer")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a correct email")
        String email,

        @NotBlank(message = "Address required")
        String address,

        @NotBlank(message = "Date of birth is required")
        String dateOfBirth,

        @NotBlank(message = "Registered date is required")
        String registeredDate
) {
}
