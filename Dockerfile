FROM maven:3.8.7-eclipse-temurin-17 AS builder
COPY pom.xml /build/
WORKDIR /build/
RUN mvn dependency:go-offline
COPY src /build/src/
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine
ARG JAR_FILE=/build/target/*.jar
RUN mkdir -p /opt/task_manager
COPY --from=builder $JAR_FILE /opt/profiler/app.jar
ENTRYPOINT ["java", "-jar", "/opt/profiler/app.jar"]