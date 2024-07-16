FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar
COPY config/application-secret.yml /app/config/application-secret.yml
COPY config/application.yml /app/config/application.yml
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dspring.config.location=/app/config/application.yml", "-Dserver.env=${ENV}", "-jar", "app.jar"]