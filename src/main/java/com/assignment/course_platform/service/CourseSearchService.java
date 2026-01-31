package com.assignment.course_platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch.core.search.InnerHits;
import com.assignment.course_platform.dto.helpers.CourseMatchDto;
import com.assignment.course_platform.dto.helpers.MatchDto;
import com.assignment.course_platform.dto.responses.SearchResponseDto;
import com.assignment.course_platform.model.CourseSearch;
import com.assignment.course_platform.model.SubtopicSearch;
import com.assignment.course_platform.model.TopicSearch;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;

@Service
public class CourseSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public CourseSearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public SearchResponseDto search(String query) {
        NativeQuery searchQuery = NativeQuery.builder()
                                             .withQuery(q -> q
                                                     .bool(b -> b
                                                             .should(s -> s.match(m -> m.field("title").query(query).fuzziness("AUTO").boost(3.0f)))
                                                             .should(s -> s.match(m -> m.field("description").query(query).fuzziness("AUTO").boost(1.5f)))
                                                             .should(s -> s.nested(n -> n
                                                                             .path("topics")
                                                                             .query(nq -> nq.match(m -> m.field("topics.title").query(query).fuzziness("AUTO").boost(2.5f)))
                                                                             .innerHits(InnerHits.of(ih -> ih.name("topics_hits"))))
                                                             )
                                                             .should(s -> s.nested(n -> n
                                                                             .path("topics.subtopics")
                                                                             .query(nq -> nq.bool(nb -> nb
                                                                                             .should(ns -> ns.match(m -> m.field("topics.subtopics.title").query(query).fuzziness("AUTO").boost(2.0f)))
                                                                                             .should(ns -> ns.match(m -> m.field("topics.subtopics.content").query(query).fuzziness("AUTO").boost(1.0f))))
                                                                             )
                                                                             .innerHits(InnerHits.of(ih -> ih.name("subtopics_hits"))))
                                                             )
                                                     )
                                             )
                .withHighlightQuery(buildHighlight()).build();

        SearchHits<CourseSearch> hits = elasticsearchOperations.search(searchQuery, CourseSearch.class);

        List<CourseMatchDto> results = hits.getSearchHits()
                .stream()
                .map(hit -> buildCourseMatch(hit, query))
                .toList();

        return new SearchResponseDto(query, results);
    }

    private CourseMatchDto buildCourseMatch(SearchHit<CourseSearch> hit, String query) {
        CourseSearch courseSearch = hit.getContent();
        List<MatchDto> matches = new ArrayList<>();

        Map<String, List<String>> highlights = hit.getHighlightFields();

        if (highlights != null && highlights.containsKey("title")) {
            matches.add(new MatchDto(
                    "title",
                    null,
                    null,
                    null,
                    highlights.get("title").get(0)
            ));
        }

        if (highlights != null && highlights.containsKey("description")) {
            matches.add(new MatchDto(
                    "description",
                    null,
                    null,
                    null,
                    highlights.get("description").get(0)
            ));
        }

        if (highlights != null && highlights.containsKey("topics.title")) {
            matches.add(new MatchDto(
                    "topic",
                    highlights.get("topics.title").get(0),
                    null,
                    null,
                    highlights.get("topics.title").get(0)
            ));
        }

        Map<String, SearchHits<?>> innerHits = hit.getInnerHits();

        if (highlights != null && highlights.containsKey("topics.subtopics.title")) {
            String subtopicId = null;
            String subtopicTitle = null;
            String parentTopicTitle = null;

            if (innerHits != null && innerHits.containsKey("subtopics_hits")) {
                SearchHits<?> subtopicsHits = innerHits.get("subtopics_hits");
                if (subtopicsHits != null && subtopicsHits.getTotalHits() > 0) {
                    SearchHit<?> firstHit = subtopicsHits.getSearchHit(0);
                    if (firstHit.getContent() instanceof SubtopicSearch) {
                        SubtopicSearch subtopicData = (SubtopicSearch) firstHit.getContent();
                        subtopicId = (String) subtopicData.getId();
                        subtopicTitle = (String) subtopicData.getTitle();
                        parentTopicTitle = findParentTopicTitle(courseSearch, subtopicId);
                    }
                }
            }

            matches.add(new MatchDto(
                    "subtopic",
                    parentTopicTitle,
                    subtopicId,
                    subtopicTitle,
                    highlights.get("topics.subtopics.title").get(0)
            ));
        }

        if (highlights != null && highlights.containsKey("topics.subtopics.content")) {
            String subtopicId = null;
            String subtopicTitle = null;
            String parentTopicTitle = null;

            if (innerHits != null && innerHits.containsKey("subtopics_hits")) {
                SearchHits<?> subtopicsHits = innerHits.get("subtopics_hits");
                if (subtopicsHits != null && subtopicsHits.getTotalHits() > 0) {
                    SearchHit<?> firstHit = subtopicsHits.getSearchHit(0);
                    if (firstHit.getContent() instanceof SubtopicSearch) {
                        SubtopicSearch subtopicData = (SubtopicSearch) firstHit.getContent();
                        subtopicId = (String) subtopicData.getId();
                        subtopicTitle = (String) subtopicData.getTitle();
                        parentTopicTitle = findParentTopicTitle(courseSearch, subtopicId);
                    }
                }
            }

            matches.add(new MatchDto(
                    "content",
                    parentTopicTitle,
                    subtopicId,
                    subtopicTitle,
                    highlights.get("topics.subtopics.content").get(0)
            ));
        }

        return new CourseMatchDto(courseSearch.getId(), courseSearch.getTitle(), matches);
    }

    private String findParentTopicTitle(CourseSearch courseSearch, String subtopicId) {
        if (courseSearch.getTopics() == null || subtopicId == null) {
            return null;
        }

        for (TopicSearch topic : courseSearch.getTopics()) {
            if (topic.getSubtopics() == null) {
                continue;
            }
            for (SubtopicSearch subtopic : topic.getSubtopics()) {
                if (subtopicId.equals(subtopic.getId())) {
                    return topic.getTitle();
                }
            }
        }

        return null;
    }

    private HighlightQuery buildHighlight() {
        List<HighlightField> fields = List.of(
                new HighlightField("title"),
                new HighlightField("description"),
                new HighlightField("topics.title"),
                new HighlightField("topics.subtopics.title"),
                new HighlightField("topics.subtopics.content")
        );

        HighlightParameters parameters = HighlightParameters.builder()
                .withPreTags("<em>")
                .withPostTags("</em>")
                .withFragmentSize(100)
                .withNumberOfFragments(1)
                .build();

        return new HighlightQuery(new Highlight(parameters, fields), null);
    }
}
