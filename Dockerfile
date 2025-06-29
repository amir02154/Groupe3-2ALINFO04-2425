FROM openjdk:17-jdk-slim
ADD Groupe12Alinfo42425.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
