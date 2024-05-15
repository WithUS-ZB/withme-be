FROM amazoncorretto:17-alpine-jdk
VOLUME /app
COPY build/libs/*.jar app.jar
ENV TZ=Asia/Seoul
RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone \
    apk del tzdata
ENTRYPOINT ["java","-jar","app.jar"]
