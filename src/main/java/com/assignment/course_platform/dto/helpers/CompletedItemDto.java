package com.assignment.course_platform.dto.helpers;

import java.time.LocalDateTime;

public record CompletedItemDto(
        String subtopicId,
        String subtopicTitle,
        LocalDateTime completedAt
) {
}
