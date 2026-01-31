package com.assignment.course_platform.dto.responses;

import java.util.List;

import com.assignment.course_platform.dto.helpers.CourseMatchDto;

public record SearchResponseDto(
        String query,
        List<CourseMatchDto> results
) {
}
