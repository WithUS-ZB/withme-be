FROM amazoncorretto:17-alpine-jdk
ENV SPRING_PROFILES_ACTIVE="dev"
WORKDIR /app
COPY build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]