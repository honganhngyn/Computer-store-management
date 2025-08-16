## Các công nghệ sử dụng trong project

- Java 21
- Spring Boot
  - Spring Boot Starter Web
  - Spring Boot Starter Thymeleaf
  - Spring Boot Starter Data JPA
  - Spring Boot Starter Security
- Thymeleaf
- JavaScript
- HTML/CSS
- MSSQL

## Cài đặt

### Yêu cầu

- Java 21
- Maven
- MSSQL

1. Cấu hình cơ sở dữ liệu trong application.properties:
   spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
   spring.datasource.username=root
   spring.datasource.password=**yourpassword**
   spring.jpa.hibernate.ddl-auto=update

2. Chạy ứng dụng
   ```bash
   mvn spring-boot run

4. Truy cập ứng dụng tại http://localhost:8080.
