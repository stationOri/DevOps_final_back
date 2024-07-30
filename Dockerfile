FROM eclipse-temurin:17-jdk-alpine
COPY ./build/libs/*SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=production
ENTRYPOINT ["java", "-jar", "app.jar"]