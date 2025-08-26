# 📖 BlogifyHub

A **Medium-like blogging platform built with Spring Boot, featuring user authentication, subscriptions, posts, comments, claps, and follows. Create, share, and discover engaging content in a clean, modern interface.**

---

## 🚀 Features

- **User Authentication & Authorization**
  - JWT-based authentication
  - Role-based access control
  - Secure password storage

- **Blog Management**
  - Create, read, update, and delete blog posts
  - Rich text editing capabilities
  - Image uploads with Cloudinary integration
  - Categorization and tagging system

- **User Experience**
  - Responsive design
  - Email notifications
  - User profiles

- **Technical Features**
  - RESTful API architecture
  - PostgreSQL database
  - Docker containerization
  - Maven build tool

---

## 🛠 Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5
- **Database**: PostgreSQL
- **Authentication**: JWT
- **File Storage**: Cloudinary
- **Email**: Spring Mail
- **Containerization**: Docker
- **Build Tool**: Maven
---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17 or higher
- Maven 3.6.3 or higher
- PostgreSQL 12 or higher
- Docker (optional)

### ⚙️ Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/HassanAlwakeel1/BlogifyHub.git
   cd BlogifyHub
   ```

2. **Configure the database**
   - Create a PostgreSQL database named `BlogifyHub`
   - Update the database credentials in `src/main/resources/application.properties`

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   The application will be available at 👉`http://localhost:2000`

### 🐳 Docker Setup

#### Option 1: Run only the Spring Boot app in Docker
1. **Build the Docker image**
   ```bash
   docker build -t blogifyhub .
   ```
2. **Run the container**
    ```bash
   docker run -p 2000:2000 blogifyhub
    ```

#### Option 2: Run Spring Boot + PostgreSQL with Docker Network
**Create a Docker network** 
```bash
docker network create blogify-network
```

**Run PostgreSQL container**
```bash
docker run --name blogify-db --network blogify-network -e POSTGRES_DB=BlogifyHub -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
```
**Run Spring Boot container on the same network**
```bash
docker run --name blogify-app --network blogify-network -e SPRING_DATASOURCE_URL=jdbc:postgresql://blogify-db:5432/BlogifyHub -e SPRING_DATASOURCE_USERNAME=postgres -e SPRING_DATASOURCE_PASSWORD=postgres -p 2000:2000 blogifyhub
```

## 📝 API Documentation

API documentation is available at `http://localhost:2000/swagger-ui.html` when the application is running.

## 🔧 Configuration

Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=2000

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/BlogifyHub
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Email Configuration (for notifications)
spring.mail.host=your_smtp_host
spring.mail.port=587
spring.mail.username=your_email@example.com
spring.mail.password=your_email_password
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ✉️ Contact

Hassan Alwakeel - [@hassanalwakeel](https://github.com/HassanAlwakeel1)

Project Link: [https://github.com/HassanAlwakeel1/BlogifyHub](https://github.com/HassanAlwakeel1/BlogifyHub)
