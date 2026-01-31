package com.assignment.course_platform.controller;

import com.assignment.course_platform.dto.responses.EnrollmentResponseDto;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.service.EnrollmentService;
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

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponseDto> enrollUser(@AuthenticationPrincipal Jwt jwt, @PathVariable String courseId) throws ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollmentService.enrollUser(jwt.getSubject(), courseId));
    }
}
