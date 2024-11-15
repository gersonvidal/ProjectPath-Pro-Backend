FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/projectpath-pro-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_projectpath-pro.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_projectpath-pro.jar"]