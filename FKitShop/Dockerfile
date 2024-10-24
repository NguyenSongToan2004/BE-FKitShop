# Chọn image Java
FROM openjdk:17-jdk-slim AS build
COPY . .
RUN mvn clean package -DskipTests

# Chọn image để chạy ứng dụng
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar FKitShop.jar
EXPOSE 8080
# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "FKitShop.jar"]