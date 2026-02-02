package com.assignment.course_platform.controller;

import com.assignment.course_platform.dto.requests.RegistrationRequestDto;
import com.assignment.course_platform.dto.responses.EnrollmentResponseDto;
import com.assignment.course_platform.dto.responses.RegistrationResponseDto;
import com.assignment.course_platform.mapper.UserRegistrationMapper;
import com.assignment.course_platform.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;

    private final UserRegistrationMapper userRegistrationMapper;

    public RegistrationController(UserRegistrationService userRegistrationService, UserRegistrationMapper userRegistrationMapper) {
        this.userRegistrationService = userRegistrationService;
        this.userRegistrationMapper = userRegistrationMapper;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully registered",
                    content = @Content(schema = @Schema(implementation = RegistrationResponseDto.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> registerUser(@Valid @RequestBody final RegistrationRequestDto registrationRequestDto) throws ValidationException {
        final var registeredUser = userRegistrationService
                .registerUser(userRegistrationMapper.toEntity(registrationRequestDto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userRegistrationMapper.toRegistrationResponseDto(registeredUser));
    }
}
