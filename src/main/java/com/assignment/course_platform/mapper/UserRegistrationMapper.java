package com.assignment.course_platform.mapper;

import com.assignment.course_platform.dto.requests.RegistrationRequestDto;
import com.assignment.course_platform.dto.responses.RegistrationResponseDto;
import com.assignment.course_platform.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

    public User toEntity(RegistrationRequestDto registrationRequestDto) {
        final var user = new User();

        user.setEmail(registrationRequestDto.email());
        user.setPassword(registrationRequestDto.password());
        user.setRole(registrationRequestDto.role());

        return user;
    }

    public RegistrationResponseDto toRegistrationResponseDto(final User user) {
        return new RegistrationResponseDto(
                user.getId(),
                user.getEmail(),
                "User registered successfully"
        );
    }
}
