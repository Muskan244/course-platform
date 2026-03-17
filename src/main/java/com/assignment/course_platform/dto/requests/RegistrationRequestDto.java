package com.assignment.course_platform.dto.requests;

import jakarta.validation.constraints.Email;

public record RegistrationRequestDto(
        @Email
        String email,
        String password,
        String role) {

}
