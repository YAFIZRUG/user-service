FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /opt/app
EXPOSE 8080
COPY --from=builder /opt/app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]