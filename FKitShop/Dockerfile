FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY /FKitShop/pom.xml .
COPY /FKitShop/src src
COPY /FKitShop/uploads uploads
RUN mvn clean package -DskipTests

RUN ls -l /app/uploads/lab

# Kiểm tra nội dung thư mục target
RUN ls -l /app/target
RUN ls -l /app/uploads


# Run Stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/FKitShop-0.0.1-SNAPSHOT.jar FKitShop.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "FKitShop.war"]