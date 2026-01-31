package com.assignment.course_platform.dto.responses;

import java.util.List;

import com.assignment.course_platform.dto.helpers.CompletedItemDto;

public record EnrollmentProgressResponseDto(
        Long enrollmentId,
        String courseId,
        String courseTitle,
        int totalSubtopics,
        int completedSubtopics,
        double completionPercentage,
        List<CompletedItemDto> completedItems
) {
}
