package com.assignment.course_platform.service;

import com.assignment.course_platform.dto.responses.EnrollmentResponseDto;
import com.assignment.course_platform.exception.AlreadyCompletedException;
import com.assignment.course_platform.exception.NotValidInputException;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.model.Course;
import com.assignment.course_platform.model.Enrollment;
import com.assignment.course_platform.model.User;
import com.assignment.course_platform.repository.CourseRepository;
import com.assignment.course_platform.repository.EnrollmentRepository;
import com.assignment.course_platform.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public EnrollmentResponseDto enrollUser(String email, String id) throws ResourceNotFoundException, NotValidInputException {
        if (id == null || id.isBlank()) {
            throw new NotValidInputException("Course id cannot be empty.");
        }

        if (!id.matches("^[a-zA-Z0-9-]+$")) {
            throw new NotValidInputException("Course id must contain only lowercase letters, uppercase letters, numbers, and hyphens.");
        }

        if (enrollmentRepository.existsByUserEmailAndCourseId(email, id)) {
            throw new AlreadyCompletedException("You are already enrolled in this course.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);

        enrollmentRepository.save(enrollment);

        return new EnrollmentResponseDto(
                enrollment.getId(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getEnrolledAt()
        );
    }
}
