package com.assignment.course_platform.seeder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.assignment.course_platform.model.Course;
import com.assignment.course_platform.model.CourseSearch;
import com.assignment.course_platform.model.Subtopic;
import com.assignment.course_platform.model.SubtopicSearch;
import com.assignment.course_platform.model.Topic;
import com.assignment.course_platform.model.TopicSearch;
import com.assignment.course_platform.repository.CourseRepository;
import com.assignment.course_platform.repository.CourseSearchRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;
    private final CourseSearchRepository courseSearchRepository;

    public DataSeeder(CourseRepository courseRepository, ObjectMapper objectMapper, CourseSearchRepository courseSearchRepository) {
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
        this.courseSearchRepository = courseSearchRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedData() throws IOException {
        try {
            if (courseRepository.count() == 0) {
                InputStream inputStream = null;

                try {
                    inputStream = new ClassPathResource("seed_data/courses.json").getInputStream();

                    List<Course> courses = objectMapper.readValue(inputStream, new TypeReference<>() {});

                    databaseSeed(courses);

                    elasticsearchIndex(courses);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void databaseSeed(List<Course> courses) {
        for (Course course : courses) {
            List<Topic> topics = course.getTopics();
            if (topics != null) {
                for (Topic topic : topics) {
                    topic.setCourse(course);
                    List<Subtopic> subtopics = topic.getSubtopics();
                    if (subtopics != null) {
                        for (Subtopic subtopic : subtopics) {
                            subtopic.setTopic(topic);
                        }
                    }
                }
            }
        }
        courseRepository.saveAll(courses);
    }

    private void elasticsearchIndex(List<Course> courses) {
        List<CourseSearch> courseSearches = courses
                .stream()
                .map(this::mapToCourseSearch)
                .toList();

        courseSearchRepository.saveAll(courseSearches);
    }

    private CourseSearch mapToCourseSearch(Course course) {
        List<TopicSearch> topicSearches = null;

        if (course.getTopics() != null) {
            topicSearches = course.getTopics()
                    .stream()
                    .map(this::mapToTopicSearch)
                    .toList();
        }

        return new CourseSearch(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                topicSearches
        );
    }

    private TopicSearch mapToTopicSearch(Topic topic) {
        List<SubtopicSearch> subtopicSearches = null;

        if (topic.getSubtopics() != null) {
            subtopicSearches = topic.getSubtopics()
                    .stream()
                    .map(this::mapToSubtopicSearch)
                    .toList();
        }

        return new TopicSearch(
                topic.getId(),
                topic.getTitle(),
                subtopicSearches
        );
    }

    private SubtopicSearch mapToSubtopicSearch(Subtopic subtopic) {
        return new SubtopicSearch(
                subtopic.getId(),
                subtopic.getTitle(),
                subtopic.getContent()
        );
    }
}
