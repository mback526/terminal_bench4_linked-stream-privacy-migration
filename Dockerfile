FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /src
COPY pom.xml .
COPY src src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /task
COPY --from=build /src/target/privacy-publisher-0.1.0.jar /opt/privacy-publisher.jar
ENTRYPOINT ["java", "-Xmx96m", "-jar", "/opt/privacy-publisher.jar"]
