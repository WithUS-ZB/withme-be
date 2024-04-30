docker run -d \
--name spring-withme \
--add-host=host.docker.internal:host-gateway \
-p 8080:8080 \
withuszb/withme:0.0.1
