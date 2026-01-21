FROM eclipse-temurin:17-jdk-alpine AS builder
LABEL author="Abraham David Lara Rodriguez"

RUN apk add --no-cache bash curl

WORKDIR /workspace

COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml pom.xml

RUN chmod +x mvnw && ./mvnw -B -ntp dependency:go-offline

COPY src src
RUN ./mvnw -B -ntp -DskipTests package

FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app
USER app

COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app/app.jar"]