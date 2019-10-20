FROM openjdk:8-jdk-alpine
ARG VERSION
EXPOSE 8080
COPY target/nasa-app-${VERSION}.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]