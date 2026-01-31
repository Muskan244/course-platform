package com.assignment.course_platform.controller;

import java.util.List;

import com.assignment.course_platform.dto.responses.CourseListResponseDto;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseListResponseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseListResponseDto> getCourseByID(@PathVariable String id) throws ResourceNotFoundException {
        return ResponseEntity.ok(courseService.getCourseByID(id));
    }
}
