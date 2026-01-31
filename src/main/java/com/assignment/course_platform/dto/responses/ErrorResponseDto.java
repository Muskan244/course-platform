package com.assignment.course_platform.dto.responses;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String error,
        String message,
        LocalDateTime timestamp
) {

    public ErrorResponseDto(String error, String message) {
        this(error, message, LocalDateTime.now());
    }
}
