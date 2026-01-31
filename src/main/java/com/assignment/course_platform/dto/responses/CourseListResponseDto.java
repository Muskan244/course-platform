package com.assignment.course_platform.dto.responses;

import java.util.List;

import com.assignment.course_platform.dto.helpers.TopicListDto;

public record CourseListResponseDto(
        String id,
        String title,
        String description,
        List<TopicListDto> topics
) {
}
