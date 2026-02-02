package com.assignment.course_platform.controller;

import com.assignment.course_platform.dto.responses.EnrollmentResponseDto;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully Enrolled",
                content = @Content(schema = @Schema(implementation = EnrollmentResponseDto.class)))
    })
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponseDto> enrollUser(@AuthenticationPrincipal Jwt jwt, @PathVariable String courseId) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollmentService.enrollUser(jwt.getSubject(), courseId));
    }
}
