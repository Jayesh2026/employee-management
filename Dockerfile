FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/employee-management-0.0.1-SNAPSHOT.jar /app/employee-management.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/employee-management.jar"]
