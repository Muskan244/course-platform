package com.assignment.course_platform.dto.responses;

import java.time.LocalDateTime;

public record SubtopicProgressResponseDto(
        String subtopicId,
        boolean completed,
        LocalDateTime completedAt
) {
}
