FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/app-dashboard-0.1.0.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=h2
ENTRYPOINT ["java", "-jar", "app.jar"]
