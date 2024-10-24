#FROM maven:3-openjdk-17 AS build
#WORKDIR /app
#COPY FKitShop .
#RUN mvn clean package -DskipTests
#
## Run Stage
#FROM openjdk:17-jdk-slim
#WORKDIR /app
#COPY --from=build /add/target/FKShop-0.0.1-SNAPSHOT.war FKShop.war
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","FKShop.war"]
# Giai đoạn build
FROM maven:3-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src            
RUN mvn clean package -DskipTests  # Biên dịch ứng dụng
RUN ls -l /app/target      # Kiểm tra thư mục target sau khi biên dịch

# Giai đoạn run
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/FKShop-0.0.1-SNAPSHOT.war FKShop.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","FKShop.war"]