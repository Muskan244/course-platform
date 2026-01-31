package com.assignment.course_platform.repository;

import java.util.List;

import com.assignment.course_platform.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUserEmailAndCourseId(String email, String id);
}
