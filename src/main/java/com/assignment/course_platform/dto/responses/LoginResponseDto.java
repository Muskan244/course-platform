package com.assignment.course_platform.dto.responses;

public record LoginResponseDto(
        String token,
        String email,
        Long expiresIn) {
}
