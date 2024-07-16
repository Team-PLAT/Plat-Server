FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=build/libs/*.jar
ARG YML_FIEL=config/application.yml
ARG YML_SECRET_FILE=config/applicaion-secret.yml
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar
COPY ${YML_FIEL} application.yml
COPY ${YML_SECRET_FILE} application-secret.yml
COPY config/application.yml /app/config/application.yml
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dspring.config.location=application.yml", "-Dserver.env=${ENV}", "-jar", "app.jar"]