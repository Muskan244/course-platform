package com.assignment.course_platform.dto.helpers;

import java.util.List;

import com.assignment.course_platform.dto.responses.SubtopicListResponseDto;

public record TopicListDto(
        String id,
        String title,
        List<SubtopicListResponseDto> subtopics
) {
}
