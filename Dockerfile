FROM amazoncorretto:17-alpine-jdk
ARG JAR_FILE=build/libs/*.jar
ARG YML_PATH=/home/ubuntu/app/config
ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dspring.config.location=${YML_PATH}/application.yml", "-Dserver.env=${ENV}", "-jar", "app.jar"]