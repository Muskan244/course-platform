package com.assignment.course_platform.dto.helpers;

import java.util.List;

public record CourseMatchDto(
        String courseId,
        String courseTitle,
        List<MatchDto> matches
) {
}
