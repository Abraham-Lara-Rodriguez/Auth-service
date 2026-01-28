# Auth Service

A secure authentication monolito modular built with **Spring Boot 4.0.1** and **JWT tokens**. This service provides user authentication, authorization, and management capabilities with PostgreSQL as the database backend.

## 🚀 Features

- **JWT Authentication** - Stateless token-based authentication with access and refresh tokens
- **User Management** - Create, read, update, and delete users with role-based access control
- **Role-Based Access Control (RBAC)** - Fine-grained permissions and authorization
- **Password Hashing** - BCrypt password encoding with configurable strength
- **CORS Support** - Configurable cross-origin requests for frontend integration
- **API Documentation** - OpenAPI 3.0 / Swagger UI for API exploration
- **Docker Support** - Containerized deployment with Docker Compose
- **Security** - Spring Security integration with JWT filters and stateless sessions

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.9.12** or higher
- **PostgreSQL 12** or higher
- **Docker** and **Docker Compose** (optional, for containerized deployment)

## 🔧 Installation

### Clone the Repository
```bash
git clone https://github.com/abrahamlara/auth-service.git
cd auth-service
```

### Set Up Environment Variables

Create a `.env` file in the project root by copying from `.env.example`:

```bash
cp .env.example .env
```

Update the `.env` file with your configuration:

```env
DB_URL=jdbc:postgresql://localhost:5433/productsdb
DB_USERNAME=root
DB_PASSWORD=1234Asd..

ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin

JWT_SECRET=your-super-secret-key-minimum-32-characters

CLIENT_ORIGIN=http://localhost:8081

PASSWORD_STRENGTH=12
```

### Build the Project

```bash
./mvnw clean package
```

## 🏃 Running the Application

### Local Execution

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8088`

### Docker Compose

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5433
- Auth Service on port 8089

To stop:
```bash
docker-compose down
```

## 📚 API Documentation

Once the application is running, access the API documentation:

- **Swagger UI**: `http://localhost:8088/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8088/v3/api-docs`

### Key Endpoints

#### Authentication
- `POST /api/v1/auth/login` - User login (returns access and refresh tokens)
- `POST /api/v1/auth/refresh-token` - Refresh access token

#### Users (Admin only)
- `GET /api/v1/users` - List all users (paginated)
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/search` - Search users with filters
- `POST /api/v1/users` - Create new user
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete (deactivate) user
- `GET /api/v1/users/profile` - Get current authenticated user profile

## 🔐 Authentication Flow

### Login
1. User sends `username` and `password` to `/api/v1/auth/login`
2. Server validates credentials and returns:
   ```json
   {
     "accessToken": "eyJhbGc...",
     "refreshToken": "eyJhbGc..."
   }
   ```

### Accessing Protected Endpoints
Include the access token in the `Authorization` header:
```
Authorization: Bearer <accessToken>
```

### Token Refresh
When the access token expires, use the refresh token:
```bash
POST /api/v1/auth/refresh-token
{
  "refreshToken": "<refreshToken>"
}
```

## 🛠️ Configuration

### application.properties

Key configuration properties:

```properties
spring.application.name=auth-service
server.port=8088

# Database
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JWT
security.jwt.secret=${JWT_SECRET}
security.jwt.accessTokenExpiration=3600000      # 1 hour
security.jwt.refreshTokenExpiration=604800000   # 7 days

# Security
spring.security.user.name=${ADMIN_USERNAME}
spring.security.user.password=${ADMIN_PASSWORD}

# CORS
CLIENT_ORIGIN=${CLIENT_ORIGIN}
```

## 👥 Roles and Permissions

### ADMIN Role
- `ADMIN_CREATE` - Create resources
- `ADMIN_READ` - Read resources
- `ADMIN_UPDATE` - Update resources
- `ADMIN_DELETE` - Delete resources

### USER Role
- `USER_READ` - Read own profile

## 🧪 Testing

Run the test suite:

```bash
./mvnw test
```

### Test Classes
- `AuthServiceImplTest` - Authentication service tests
- `UserServiceImplTest` - User service tests
- `JwtServiceTest` - JWT token generation and validation tests

## 📁 Project Structure

```
auth-service/
├── src/
│   ├── main/
│   │   ├── java/com/abrahamlara/authservice/
│   │   │   ├── auth/              # Authentication module
│   │   │   │   ├── config/        # JWT and security config
│   │   │   │   ├── controller/    # Auth endpoints
│   │   │   │   ├── dto/           # Data transfer objects
│   │   │   │   └── service/       # Business logic
│   │   │   ├── user/              # User management module
│   │   │   │   ├── controller/
│   │   │   │   ├── dto/
│   │   │   │   ├── model/         # JPA entities
│   │   │   │   ├── repository/    # Data access
│   │   │   │   ├── service/
│   │   │   │   └── mapper/        # Entity mappers
│   │   │   ├── config/            # Global configuration
│   │   │   └── shared/            # Shared utilities and exceptions
│   │   └── resources/
│   │       ├── application.properties
│   │       └── banner.txt
│   └── test/                      # Unit tests
├── Dockerfile
├── docker-compose.yaml
├── pom.xml
└── README.md
```

## 🐛 Error Handling

The API returns errors in RFC 7807 Problem Details format:

```json
{
  "type": "https://authservice/errors/unauthorized",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Invalid or expired token",
  "instance": "/api/v1/users/profile",
  "timestamp": "2026-01-21T10:30:45Z",
  "code": "UNAUTHORIZED"
}
```

## 🔒 Security Considerations

- **JWT Secret** - Change the `JWT_SECRET` in production (minimum 32 characters)
- **HTTPS** - Always use HTTPS in production
- **Token Expiration** - Access tokens expire in 1 hour, refresh tokens in 7 days
- **CORS** - Configure `CLIENT_ORIGIN` to restrict cross-origin requests
- **Password Strength** - BCrypt strength configurable via `PASSWORD_STRENGTH` (10-12 recommended)

## 📦 Dependencies

### Core
- `spring-boot-starter-webmvc` - Web framework
- `spring-boot-starter-security` - Security framework
- `spring-boot-starter-data-jpa` - ORM and database
- `spring-security-test` - Security testing

### Database
- `postgresql` - PostgreSQL driver

### API Documentation
- `springdoc-openapi-starter-webmvc-ui` - Swagger/OpenAPI integration

### JWT
- `jjwt` - JSON Web Token library

### Testing
- `spring-boot-starter-test` - JUnit 5, Mockito, AssertJ
- `testcontainers` - Integration testing with PostgreSQL

## 🚀 Deployment

### Docker Build

```bash
docker build -t auth-service:1.0 .
```

### Push to Registry

```bash
docker tag auth-service:1.0 your-registry/auth-service:1.0
docker push your-registry/auth-service:1.0
```

## 📖 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT Introduction](https://jwt.io/introduction)
- [RFC 7807 - Problem Details for HTTP APIs](https://tools.ietf.org/html/rfc7807)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👤 Author

**Abraham David Lara Rodriguez**

---

For questions or support, please open an issue in the repository.
