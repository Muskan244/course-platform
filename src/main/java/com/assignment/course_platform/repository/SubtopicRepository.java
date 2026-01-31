package com.assignment.course_platform.repository;

import com.assignment.course_platform.model.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, String> {

    @Query("SELECT COUNT(s) FROM Subtopic s WHERE s.topic.course.id = :courseId")
    int countByCourseId(@Param("courseId") String courseId);
}
