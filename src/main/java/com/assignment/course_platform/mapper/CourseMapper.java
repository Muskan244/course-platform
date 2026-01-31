package com.assignment.course_platform.mapper;

import java.util.List;

import com.assignment.course_platform.dto.responses.CourseListResponseDto;
import com.assignment.course_platform.dto.responses.SubtopicListResponseDto;
import com.assignment.course_platform.dto.helpers.TopicListDto;
import com.assignment.course_platform.model.Course;
import com.assignment.course_platform.model.Subtopic;
import com.assignment.course_platform.model.Topic;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseListResponseDto toListDto(Course course) {
        List<TopicListDto> topics = course
                .getTopics()
                .stream()
                .map(this::toTopicDto)
                .toList();

        return new CourseListResponseDto(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                topics
        );
    }

    private TopicListDto toTopicDto(Topic topic) {
        List<SubtopicListResponseDto> subtopics = topic
                .getSubtopics()
                .stream()
                .map(this::toSubtopicDto)
                .toList();

        return new TopicListDto(
                topic.getId(),
                topic.getTitle(),
                subtopics
        );
    }

    private SubtopicListResponseDto toSubtopicDto(Subtopic subtopic) {
        return new SubtopicListResponseDto(
                subtopic.getId(),
                subtopic.getTitle(),
                subtopic.getContent()
        );
    }
}
