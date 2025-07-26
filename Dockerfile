FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY /build/libs/*.jar /app/digital-report.jar
EXPOSE 11005
ENTRYPOINT ["java","-jar","digital-report.jar"]