# Course Platform API

A backend service for a learning platform where users can browse courses, enroll, and track their learning progress.

## Demo

**Deployed URL (with Swagger UI):** https://course-platform-api-ofoj.onrender.com/swagger-ui.html

## Tech Stack

- Java
- Spring Boot
- PostgreSQL
- Spring Data JPA / Hibernate
- Spring Security (JWT with RSA)
- Elasticsearch
- Swagger
- Docker

## Features

- Browse and search course content (public)
- User registration and JWT authentication
- Course enrollment (authenticated)
- Progress tracking by marking subtopics as completed (authenticated)
- Full-text search with Elasticsearch

## API Reference

### Public Endpoints

| Method | Endpoint     | Description                |
| :-------- | :------- | :------------------------- |
| `GET` | `/api/v1/courses` | Get all courses |
| `GET` | `/api/v1/courses/{id}` | Get course details |
| `GET` | `/api/v1/search?query=` | Search courses |
| `POST` | `/api/v1/auth/register` | Register new user |
| `POST` | `/api/v1/auth/login` | Login and get JWT |

### Authenticated Endpoints

| Method | Endpoint     | Description                |
| :-------- | :------- | :------------------------- |
| `POST` | `/api/v1/courses/{courseId}/enroll` | Enroll in a course |
| `POST` | `/api/v1/subtopics/{subtopicId}/complete` | Mark subtopic as completed |
| `GET` | `/api/v1/enrollments/{enrollmentId}/progress` | View enrollment progress |

## Run Locally

### Prerequisites

- Java 21
- PostgreSQL
- Elasticsearch

### Environment Variables

To run this project, add the following environment variables to your .env file:-

`SPRING_DATASOURCE_URL`

`SPRING_DATASOURCE_USERNAME`

`SPRING_DATASOURCE_PASSWORD`

`SECURITY_JWT_ELASTICSEARCH_URIS`

`SECURITY_JWT_ELASTICSEARCH_USERNAME`

`SECURITY_JWT_ELASTICSEARCH_PASSWORD`

`SECURITY_JWT_PRIVATE_KEY`

`SECURITY_JWT_PUBLIC_KEY`

`SECURITY_JWT_TTL`

`SPRINGDOC_API_DOCS_PATH`

`SPRINGDOC_API_DOCS_SWAGGER_UI`

### Build and Run

```bash
./gradlew clean bootRun
```

## Seed Data

- Course data is automatically loaded on application startup if the database is empty.

- Seed data is located at `src/main/resources/seed_data/courses.json`.

## Search Implementation

Search is implemented using **Elasticsearch** with the following features:

- Case-insensitive matching
- Partial matching using n-gram tokenizer (2-15 characters)
- Fuzzy matching for typo tolerance
- Field boosting - title matches ranked higher than content matches
- Query highlighting - matched terms are highlighted in results

Search queries match against:
- Course titles and descriptions
- Topic titles
- Subtopic titles and content

## Assumptions

- **API Versioning:** All endpoints are prefixed with `/api/v1/` for future versioning support.
- **JWT Configuration**: RSA key pair is generated during Docker build. For local development, keys must be manually generated or provided.
- **Idempotent Completion**: Marking a subtopic as complete multiple times returns the same response without error.
- **Enrollment Validation**: Users can only mark subtopics as complete for courses they are enrolled in.
- **Progress Access Control**: Users can only view progress for their enrollments.
- **Search Index Sync**: Elasticsearch index is populated alongside PostgreSQL during seed data loading.
- **String IDs**: Courses, topics, and subtopics use human-readable string IDs instead of auto-generated numeric IDs for better API usability.
- **Dual Storage**: PostgreSQL serves as the primary data store; Elasticsearch is used exclusively for search functionality.
- **Comprehensive Match Results**: Search results include all matched locations - if a query matches in course title, topic title, subtopic title, and content, each    
  match is listed separately in the response with its context snippet.

## Error Handling

The API returns consistent error messages:

```
{
    "error": "Error type",
    "message": "Description of the error",
    "timestamp": "2026-02-02T10:30:00Z"
}
```

HTTP Status Codes:
- `200`: Success
- `201`: Created (enrollment, registration)
- `400`: Bad request
- `401`: Unauthorized
- `403`: Forbidden
- `404`: Not found
- `409`: Conflict (duplicate enrollment)
