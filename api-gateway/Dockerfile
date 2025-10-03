FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# ----------------------------------------------------------------------------------

FROM openjdk:17.0.1-jdk-slim
RUN groupadd --system appuser && useradd --system -g appuser appuser
USER appuser

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]