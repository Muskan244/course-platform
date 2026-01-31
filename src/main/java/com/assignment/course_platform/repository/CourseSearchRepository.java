package com.assignment.course_platform.repository;

import com.assignment.course_platform.model.CourseSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSearchRepository extends ElasticsearchRepository<CourseSearch, String> {
}
