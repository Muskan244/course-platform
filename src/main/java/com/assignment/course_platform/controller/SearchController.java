package com.assignment.course_platform.controller;

import com.assignment.course_platform.dto.responses.SearchResponseDto;
import com.assignment.course_platform.service.CourseSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SearchController {

    private final CourseSearchService courseSearchService;

    public SearchController(CourseSearchService courseSearchService) {
        this.courseSearchService = courseSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDto> search(@RequestParam String query) {
        return ResponseEntity.ok(courseSearchService.search(query));
    }
}

