package com.assignment.course_platform.service;

import java.util.List;

import com.assignment.course_platform.dto.responses.CourseListResponseDto;
import com.assignment.course_platform.exception.NotValidInputException;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.mapper.CourseMapper;
import com.assignment.course_platform.model.Course;
import com.assignment.course_platform.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public Course createCourses(Course course) {
        return courseRepository.save(course);
    }

    public List<CourseListResponseDto> getAllCourses() {
        return courseRepository
                .findAll()
                .stream()
                .map(courseMapper::toListDto)
                .toList();
    }

    public CourseListResponseDto getCourseByID(String id) throws ResourceNotFoundException {
        if (id == null || id.isBlank()) {
            throw new NotValidInputException("Course id cannot be empty.");
        }

        if (!id.matches("^[a-zA-Z0-9-]+$")) {
            throw new NotValidInputException("Course id must only contaion lowercase letters, uppercase letters, numbers, and hyphens.");
        }

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with given id."));

        return courseMapper.toListDto(course);
    }
}
