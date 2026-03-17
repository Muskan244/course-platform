package com.assignment.course_platform.controller;

import com.assignment.course_platform.dto.responses.EnrollmentProgressResponseDto;
import com.assignment.course_platform.dto.responses.EnrollmentResponseDto;
import com.assignment.course_platform.dto.responses.SubtopicProgressResponseDto;
import com.assignment.course_platform.exception.EnrollmentAccessDeniedException;
import com.assignment.course_platform.exception.NotEnrolledException;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.service.SubtopicProgressService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SubtopicProgressController {

    private final SubtopicProgressService subtopicProgressService;

    public SubtopicProgressController(SubtopicProgressService subtopicProgressService) {
        this.subtopicProgressService = subtopicProgressService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully completed subtopic",
                    content = @Content(schema = @Schema(implementation = SubtopicProgressResponseDto.class)))
    })
    @PostMapping("/subtopics/{subtopicId}/complete")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<SubtopicProgressResponseDto> markSubtopicComplete(@AuthenticationPrincipal Jwt jwt, @PathVariable String subtopicId) throws NotEnrolledException, ResourceNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subtopicProgressService.markSubtopicComplete(jwt.getSubject(), subtopicId));
    }

    @GetMapping("/enrollments/{enrollmentId}/progress")
    public ResponseEntity<EnrollmentProgressResponseDto> getEnrollmentProgress(@AuthenticationPrincipal Jwt jwt, @PathVariable @Positive(message = "Enrollement id must be a positive number.") Long enrollmentId) throws ResourceNotFoundException, EnrollmentAccessDeniedException {
        return ResponseEntity.ok(subtopicProgressService.getEnrollmentProgress(enrollmentId, jwt.getSubject()));
    }
}
