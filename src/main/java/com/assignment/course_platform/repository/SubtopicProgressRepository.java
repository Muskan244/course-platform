package com.assignment.course_platform.repository;

import java.util.List;

import com.assignment.course_platform.model.SubtopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtopicProgressRepository extends JpaRepository<SubtopicProgress, Long> {

    List<SubtopicProgress> findByUserEmailAndSubtopicId(String email, String id);

    @Query("SELECT sp FROM SubtopicProgress sp " +
            "WHERE sp.user.email = :userEmail " +
            "AND sp.subtopic.topic.course.id = :courseId")
    List<SubtopicProgress> findByUserEmailAndCourseId(@Param("userEmail") String userEmail, @Param("courseId") String courseId);
}
