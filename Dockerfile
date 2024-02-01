FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/springboot-mysql-docker.jar springboot-mysql-docker.jar
ENTRYPOINT ["java", "-jar", "/springboot-mysql-docker.jar"]
EXPOSE 8081