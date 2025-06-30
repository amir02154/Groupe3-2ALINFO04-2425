FROM openjdk:17
WORKDIR /app
COPY target/Groupe12Alinfo42425.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

