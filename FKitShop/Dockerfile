FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from-build /add/target/FKShop-0.0.1-SNAPSHOT.war FKShop.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","FKShop.war"]