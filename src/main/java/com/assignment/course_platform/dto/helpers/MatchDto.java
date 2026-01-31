package com.assignment.course_platform.dto.helpers;

public record MatchDto(
        String type,
        String topicTitle,
        String subtopicId,
        String subtopicTitle,
        String snippet
) {
}
