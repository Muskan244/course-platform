package com.assignment.course_platform.model;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class TopicSearch {

    @Field(type = FieldType.Keyword, index = false)
    private String id;

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Nested)
    private List<SubtopicSearch> subtopics;

    public TopicSearch(String id, String title, List<SubtopicSearch> subtopics) {
        this.id = id;
        this.title = title;
        this.subtopics = subtopics;
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

    public List<SubtopicSearch> getSubtopics() {
        return subtopics;
    }

    public void setSubtopics(List<SubtopicSearch> subtopics) {
        this.subtopics = subtopics;
    }
}
