FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .

RUN mkdir -p src/main/resources/jwt && \
    openssl genrsa -out src/main/resources/jwt/app.key 2048 && \
    openssl rsa -in src/main/resources/jwt/app.key -pubout -out src/main/resources/jwt/app.pub

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]