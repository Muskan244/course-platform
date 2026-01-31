package com.assignment.course_platform.model;

import java.util.List;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "search-course-platform")
@Setting(settingPath = "elasticsearch/search-course-platform-settings.json")
public class CourseSearch {

    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String description;

    @Field(type = FieldType.Nested)
    private List<TopicSearch> topics;

    public CourseSearch(String id, String title, String description, List<TopicSearch> topics) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.topics = topics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TopicSearch> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicSearch> topics) {
        this.topics = topics;
    }
}
