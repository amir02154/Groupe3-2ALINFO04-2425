FROM openjdk:17
WORKDIR /app
COPY foyer.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
