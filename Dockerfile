FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY /build/libs/*.jar /app/digital-report.jar
RUN mkdir -p /app/db
EXPOSE 11005
ENTRYPOINT ["java","-jar","digital-report.jar"]