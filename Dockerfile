FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]
COPY config/application.yml /config/application.yml
COPY config/application-blue.yml /config/application-blue.yml
COPY config/application-green.yml /config/application-green.yml
COPY config/application-dev.yml /config/application-dev.yml
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dspring.config.location=file:/config/application.yml", "-Dserver.env=${ENV}", "-jar", "app.jar"]