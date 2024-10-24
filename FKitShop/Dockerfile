# Chọn image Java
FROM openjdk:17-jdk-slim AS builder

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file cấu hình Maven và thư mục source
COPY pom.xml .
COPY src ./src

# Build ứng dụng
RUN ./mvnw clean package -DskipTests

# Chọn image để chạy ứng dụng
FROM openjdk:17-jdk-slim
COPY --from=builder /app/target/your-app-name.jar app.jar

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "/app.jar"]