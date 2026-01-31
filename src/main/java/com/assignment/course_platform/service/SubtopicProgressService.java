package com.assignment.course_platform.service;

import java.util.List;

import com.assignment.course_platform.dto.helpers.CompletedItemDto;
import com.assignment.course_platform.dto.responses.EnrollmentProgressResponseDto;
import com.assignment.course_platform.dto.responses.SubtopicProgressResponseDto;
import com.assignment.course_platform.exception.AlreadyCompletedException;
import com.assignment.course_platform.exception.EnrollmentAccessDeniedException;
import com.assignment.course_platform.exception.NotEnrolledException;
import com.assignment.course_platform.exception.NotValidInputException;
import com.assignment.course_platform.exception.ResourceNotFoundException;
import com.assignment.course_platform.model.Course;
import com.assignment.course_platform.model.Enrollment;
import com.assignment.course_platform.model.Subtopic;
import com.assignment.course_platform.model.SubtopicProgress;
import com.assignment.course_platform.model.User;
import com.assignment.course_platform.repository.CourseRepository;
import com.assignment.course_platform.repository.EnrollmentRepository;
import com.assignment.course_platform.repository.SubtopicProgressRepository;
import com.assignment.course_platform.repository.SubtopicRepository;
import com.assignment.course_platform.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SubtopicProgressService {

    private final SubtopicProgressRepository subtopicProgressRepository;

    private final SubtopicRepository subtopicRepository;

    private final UserRepository userRepository;

    private final EnrollmentRepository enrollmentRepository;

    public SubtopicProgressService(SubtopicProgressRepository subtopicProgressRepository, SubtopicRepository subtopicRepository, UserRepository userRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.subtopicProgressRepository = subtopicProgressRepository;
        this.subtopicRepository = subtopicRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public SubtopicProgressResponseDto markSubtopicComplete(String email, String id) throws ResourceNotFoundException, NotEnrolledException {
        if (id == null || id.isBlank()) {
            throw new NotValidInputException("Subtopic id cannot be empty.");
        }

        if (!id.matches("^[a-zA-Z0-9-]+$")) {
            throw new NotValidInputException("Subtopic id must contain only lowercase letters, uppercase letters, numbers, and hyphens.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Subtopic subtopic = subtopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtopic not found"));

        Course course = subtopic.getTopic().getCourse();

        boolean isEnrolled = enrollmentRepository.existsByUserEmailAndCourseId(user.getEmail(), course.getId());

        if (!isEnrolled) {
            throw new NotEnrolledException("You must be enrolled in the course for progress tracking.");
        }

        List<SubtopicProgress> existingProgress = subtopicProgressRepository.findByUserEmailAndSubtopicId(user.getEmail(), subtopic.getId());

        if (!existingProgress.isEmpty()) {
            throw new AlreadyCompletedException("You have already completed this subtopic.");
        }

        SubtopicProgress subtopicProgress = new SubtopicProgress();
        subtopicProgress.setUser(user);
        subtopicProgress.setSubtopic(subtopic);

        subtopicProgressRepository.save(subtopicProgress);

        return new SubtopicProgressResponseDto(
                subtopic.getId(),
                subtopicProgress.isCompleted(),
                subtopicProgress.getCompletedAt()
        );
    }

    public EnrollmentProgressResponseDto getEnrollmentProgress(Long enrollmentId, String email) throws EnrollmentAccessDeniedException, ResourceNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (!enrollment.getUser().getId().equals(user.getId())) {
            throw new EnrollmentAccessDeniedException("This enrollment does not belong to you.");
        }

        Course course = enrollment.getCourse();

        int totalSubtopics = subtopicRepository.countByCourseId(course.getId());

        List<SubtopicProgress> completedProgress = subtopicProgressRepository.findByUserEmailAndCourseId(user.getEmail(), course.getId());

        int completedCount = completedProgress.size();

        double completionPercentage = calculatePercentage(completedCount, totalSubtopics);

        List<CompletedItemDto> completedItems = completedProgress.stream()
                                                                 .map(subtopicProgress -> new CompletedItemDto(
                        subtopicProgress.getSubtopic().getId(),
                        subtopicProgress.getSubtopic().getTitle(),
                        subtopicProgress.getCompletedAt()
                )).toList();

        return new EnrollmentProgressResponseDto(
                enrollment.getId(),
                course.getId(),
                course.getTitle(),
                totalSubtopics,
                completedCount,
                completionPercentage,
                completedItems
        );
    }

    private double calculatePercentage(int completedCount, int totalSubtopics) {
        if (totalSubtopics == 0) {
            return 0.0;
        }
        double percentage = ((double) completedCount / totalSubtopics) * 100;
        return Math.round(percentage * 100.0) / 100.0;
    }
}
