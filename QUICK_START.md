# Quick Start Guide - NRCC Backend

## 1. Prerequisites

```bash
# Check Java version (requires Java 17+)
java -version

# Check Maven
mvn -version

# Check PostgreSQL
psql --version
```

## 2. Database Setup

```bash
# Create database
createdb nrcc_dev_db

# Or using psql
psql -U postgres
CREATE DATABASE nrcc_dev_db;
\q
```

## 3. Configuration

Edit `src/main/resources/application-dev.properties`:

```properties
# Update these if needed
spring.datasource.url=jdbc:postgresql://localhost:5432/nrcc_dev_db
spring.datasource.username=postgres
spring.datasource.password=your_password

# Update email settings (optional for testing)
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## 4. Build & Run

```bash
# Navigate to backend directory
cd /Users/mwendavano/road-fund/nrcc-backend

# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

Application starts on: `http://localhost:8080/api`

## 5. Test the API

### Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "role": "PUBLIC_APPLICANT"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

Save the `accessToken` from the response!

### Test User Management (Admin Only)

First, login as admin (default password: `password123`):
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nrcc.go.tz",
    "password": "password123"
  }'
```

Then use the admin token:
```bash
# List all users
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"

# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "john123",
    "phoneNumber": "+255712345678",
    "role": "PUBLIC_APPLICANT"
  }'
```

## 6. Access Documentation

- **Swagger UI:** http://localhost:8080/api/swagger-ui.html
- **API Docs:** http://localhost:8080/api/v3/api-docs
- **Health Check:** http://localhost:8080/api/actuator/health

## 7. Default Test Users

Run `database/init.sql` to create test users. All have password: `password123`

| Email | Role | Description |
|-------|------|-------------|
| admin@nrcc.go.tz | SYSTEM_ADMINISTRATOR | Full access |
| minister@works.go.tz | MINISTER_OF_WORKS | Minister |
| chair@nrcc.go.tz | NRCC_CHAIRPERSON | NRCC Chair |
| member1@nrcc.go.tz | NRCC_MEMBER | NRCC Member |
| rc@dodoma.go.tz | REGIONAL_COMMISSIONER | RC Dodoma |
| ras@dodoma.go.tz | REGIONAL_ADMINISTRATIVE_SECRETARY | RAS Dodoma |
| public@example.com | PUBLIC_APPLICANT | Public User |

## 8. Common Commands

```bash
# Compile only
mvn compile

# Run tests
mvn test

# Package as JAR
mvn package

# Clean build directory
mvn clean

# Skip tests
mvn package -DskipTests
```

## 9. File Structure

```
nrcc-backend/
├── src/main/java/tz/go/roadsfund/nrcc/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # JPA entities
│   ├── enums/          # Enumerations
│   ├── exception/      # Custom exceptions
│   ├── repository/     # Data repositories
│   ├── security/       # Security components
│   ├── service/        # Business logic
│   └── util/           # Utilities
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
├── database/
│   └── init.sql        # Sample data
├── README.md           # Main documentation
├── USER_MANAGEMENT.md  # User management guide
├── IMPLEMENTATION_SUMMARY.md  # What was built
└── QUICK_START.md      # This file
```

## 10. Troubleshooting

### Port Already in Use
```bash
# Find and kill process on port 8080
lsof -ti:8080 | xargs kill -9
```

### Database Connection Error
```bash
# Check PostgreSQL is running
pg_isready

# Start PostgreSQL (macOS)
brew services start postgresql
```

### Compilation Errors
```bash
# Clean and rebuild
mvn clean install -DskipTests
```

## 11. Environment Profiles

```bash
# Run in development mode (default)
mvn spring-boot:run

# Run in production mode
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 12. API Endpoints Summary

### Authentication
- `POST /api/auth/register` - Register
- `POST /api/auth/login` - Login
- `POST /api/auth/logout` - Logout

### Users (Admin)
- `POST /api/users` - Create user
- `GET /api/users` - List users
- `GET /api/users/{id}` - Get user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Files
- `POST /api/files/upload` - Upload file
- `GET /api/files/download/{fileName}` - Download file
- `DELETE /api/files/{fileName}` - Delete file

## 13. Next Steps

After getting the backend running:

1. Test all user management endpoints
2. Implement application management
3. Connect with Angular frontend
4. Add workflow processing
5. Implement reporting

## 14. Documentation

- **README.md** - Full documentation
- **USER_MANAGEMENT.md** - User management & permissions
- **IMPLEMENTATION_SUMMARY.md** - What was implemented
- **QUICK_START.md** - This guide

## 15. Support

For issues:
1. Check logs: `logs/nrcc-backend.log`
2. Review error messages carefully
3. Verify database connection
4. Check application.properties

Happy coding! 🚀
