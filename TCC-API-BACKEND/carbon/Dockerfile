FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar
# COPY target/* /app

ENV api.authorization.token='MjAyNTpncnVwb1RDQ3BlZ2FkYWRlQ2FyYm9ubw=='

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
