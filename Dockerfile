FROM maven:3.9.6-eclipse-temurin-21-alpine as dev-build



WORKDIR /app
COPY . .
RUN ["mvn","clean","package","-DskipTests"]

EXPOSE 8080

CMD ["java","-jar","target/Back-End-Projeto-SD-0.0.1-SNAPSHOT.jar"]