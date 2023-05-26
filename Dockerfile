# Sử dụng một image cơ bản chứa JDK 17 (phiên bản slim)
FROM openjdk:17-jdk-slim

# Thay đổi thư mục làm việc mặc định trong container
WORKDIR /app

# Sao chép tệp build.gradle và settings.gradle vào container
COPY build.gradle .
COPY settings.gradle .

# Sao chép các tệp Gradle Wrapper vào container
COPY gradle gradle

COPY gradlew .

# Sao chép mã nguồn của dự án vào container
COPY src src


# Chạy quá trình xây dựng dự án
RUN ./gradlew build -x test

# Sao chép tệp jar vào container
RUN cp build/libs/*.jar bloom.jar

# Định nghĩa lệnh mặc định để chạy ứng dụng Spring Boot
CMD ["java", "-jar", "bloom.jar"]
