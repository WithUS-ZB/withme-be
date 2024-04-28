FROM amazoncorretto:17-alpine-jdk
VOLUME /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]