package com.assignment.course_platform.dto.responses;

import java.time.LocalDateTime;

public record EnrollmentResponseDto(
        Long enrollmentId,
        String courseId,
        String courseTitle,
        LocalDateTime enrolledAt
) {
}
