E-Commerce Backend Application
A secure and scalable E-Commerce backend application built using Java, Spring Boot, Spring Security (JWT-based), Hibernate, and MySQL. The application follows layered architecture with role-based access, validation, exception handling, and modular packaging.
 Features
✅ User Authentication & Authorization (JWT)
✅ Role-based access control
✅ Product and Cart module integration
✅ Exception Handling (Global and Custom)
✅ Spring Security integration
✅ Validation for all inputs
✅ Modular folder structure
✅ RESTful API development
✅ Repository-Service-Controller architecture
✅ Object mapping using DTOs (payload)

📁 Project Structure

src/
├── config              # General application-level configurations
├── controller          # REST Controllers for API endpoints
├── exception           # Custom and global exception handlers
├── model               # JPA Entities (User, Product, Cart, etc.)
├── payload             # DTOs for requests and responses
├── repositories        # Spring Data JPA Repositories
├── secrity/jwt         # JWT token generation and validation
├── security            # Spring Security config
├── service             # Interfaces for service layers
├── serviceimpl         # Implementations for business logic
├── util                # Utility/helper classes
└── ECommerceApplication.java # Main Spring Boot entry point

🔐 Security

JWT-based login system
Password hashing with BCrypt
Endpoint access restricted based on user roles (Admin, Customer)

| Layer        | Technology                   |
| ------------ | ---------------------------- |
| Backend      | Java 17, Spring Boot         |
| Security     | Spring Security, JWT         |
| ORM/Database | Hibernate, MySQL             |
| Validation   | Javax Validation             |
| Build Tool   | Maven                        |
| IDE          | Spring Tool Suite / IntelliJ |

Setup Instructions
Prerequisites:

Java 17
Maven
MySQL

git clone https://github.com/RakshitDev/E-Commerce.git
cd E-Commerce

spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=yourUsername
spring.datasource.password=yourPassword

mvn spring-boot:run
| Method | Endpoint         | Description       |
| ------ | ---------------- | ----------------- |
| POST   | /api/auth/signup | Register new user |
| POST   | /api/auth/signin | Login and get JWT |
| GET    | /api/products    | View all products |
| POST   | /api/cart/add    | Add item to cart  |

🙌 Author
Rakshit Alagundi
Java Backend Developer
🔗 GitHub



