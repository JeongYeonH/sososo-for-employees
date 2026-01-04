
FROM amazoncorretto:17-alpine


WORKDIR /app


COPY build/libs/back-0.0.1-SNAPSHOT.jar /app/deploy-my-server.jar


CMD ["java", "-jar", "deploy-my-server.jar"]


EXPOSE 4040