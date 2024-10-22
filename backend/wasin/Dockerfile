# 1. build
FROM gradle:7.5-jdk17 as build

WORKDIR /home/gradle/project

COPY . .

#ENV TESTCONTAINERS_HOST_OVERRIDE=host.docker.internal
RUN gradle wrapper
RUN ./gradlew clean build -x test

# 2. Run the application
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /opt/wasin

COPY --from=build /home/gradle/project/build/libs/wasin-0.0.1-SNAPSHOT.jar .

ENV DB_URL=${DB_LOCAL_URL}
ENV DB_PASSWORD=${DB_LOCAL_PASSWORD}

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "wasin-0.0.1-SNAPSHOT.jar"]

