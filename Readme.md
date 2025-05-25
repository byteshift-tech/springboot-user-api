# Spring Boot User API

A secure and modular REST API for user registration and login, built with Spring Boot and JWT. Ideal for modern applications requiring robust authentication and clean code architecture.

---

## 🔧 Features

* 🔐 User registration with input validation
* 🔑 Secure login with JWT token generation
* 🔒 Password encryption using BCrypt
* 🧹 Modular structure (controller, service, repository, security)
* 🛡️ Role-based access (extensible)
* 📓 MySQL database integration via Spring Data JPA
* 📆 Exception handling and structured responses
* 🧪 Unit tests with JUnit 5 and Mockito
* 📊 Code coverage with JaCoCo

---

## 💪 Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Security
* Spring Data JPA
* MySQL
* JWT (JJWT)
* Gradle
* JUnit 5, Mockito
* JaCoCo (test coverage)

---

## 🚀 Getting Started

### 1. Clone the project

```bash
git clone https://github.com/ByteShiftTech/springboot-user-api.git
cd springboot-user-api
```

### 2. Configure your database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/userdb
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
```

### 3. Run the application

```bash
./gradlew bootRun
```

---

## 🧪 Run Tests

```bash
./gradlew test
```

JaCoCo will generate code coverage reports in `/build/reports/jacoco/test/html/`

---

## 📬 API Endpoints

| Method | Endpoint             | Description              |
| ------ | -------------------- | ------------------------ |
| POST   | `/api/auth/register` | Register new user        |
| POST   | `/api/auth/login`    | Authenticate and get JWT |

> Swagger UI or Postman collection can be added for testing.

---

## 📂 Project Structure

```
src/main/java/com/byteshifttech/userapi/
├── controller
├── dto
├── entity
├── repository
├── security
├── service
└── UserApiApplication.java
```

---

## 👤 Author

**ByteShiftTech**
Backend Developer | Java & Spring Boot Specialist
[GitHub Profile](https://github.com/ByteShiftTech)

---

## 📄 License

This project is open-source and available for reuse with credit.
