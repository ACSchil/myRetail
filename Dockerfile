FROM openjdk:8-jdk-alpine
COPY build/libs/myRetail-1.0.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=gcp","-jar","/app.jar"]