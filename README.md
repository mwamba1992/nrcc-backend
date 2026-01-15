# NRCC Database Management System - Backend API

Backend API service for the National Road Classification Committee (NRCC) Database Management System.

## Overview

The NRCC system manages road reclassification requests from District to Regional or Trunk class roads in Tanzania. This Spring Boot application provides the REST API backend for the system, handling:

- Road reclassification applications
- Workflow routing and approvals
- Eligibility validation
- NRCC verification and recommendations
- Minister decisions and gazettement
- Action plan management
- Notifications (Email/SMS)

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.1
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA (Hibernate)
- **Build Tool**: Maven
- **Documentation**: Springdoc OpenAPI (Swagger)

## Prerequisites

Before running the application, ensure you have:

- Java 17 or higher installed
- Maven 3.6+ installed
- PostgreSQL 12+ installed and running
- SMTP server access (for email notifications)

## Project Structure

```
nrcc-backend/
├── src/
│   ├── main/
│   │   ├── java/tz/go/roadsfund/nrcc/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── request/      # Request DTOs
│   │   │   │   └── response/     # Response DTOs
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── enums/            # Enumerations
│   │   │   ├── exception/        # Custom exceptions
│   │   │   ├── repository/       # JPA repositories
│   │   │   ├── security/         # Security components
│   │   │   ├── service/          # Business logic
│   │   │   └── util/             # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       └── application-prod.properties
│   └── test/                     # Test classes
└── pom.xml                       # Maven configuration
```

## Database Setup

1. **Create Database**:
```sql
CREATE DATABASE nrcc_dev_db;
CREATE USER nrcc_user WITH PASSWORD 'nrcc_password';
GRANT ALL PRIVILEGES ON DATABASE nrcc_dev_db TO nrcc_user;
```

2. **Update Configuration**:
Edit `src/main/resources/application-dev.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nrcc_dev_db
spring.datasource.username=nrcc_user
spring.datasource.password=nrcc_password
```

## Installation & Setup

### 1. Clone the Repository
```bash
cd /path/to/road-fund
cd nrcc-backend
```

### 2. Configure Application Properties

**Development Environment** (`application-dev.properties`):
- Database connection
- Email/SMS settings
- File upload directory
- JWT secret (generate a strong secret for production)

**Production Environment** (`application-prod.properties`):
- Use environment variables for sensitive data
- Set appropriate security settings

### 3. Build the Application
```bash
mvn clean install
```

### 4. Run the Application

**Development Mode**:
```bash
mvn spring-boot:run
```

Or using the JAR:
```bash
java -jar target/nrcc-backend-1.0.0.jar
```

The application will start on `http://localhost:8080/api`

## Configuration

### JWT Configuration
Update in `application.properties`:
```properties
jwt.secret=your-secret-key-at-least-256-bits
jwt.expiration=86400000
jwt.refresh-expiration=604800000
```

### Email Configuration
Update SMTP settings:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### File Upload Configuration
```properties
file.upload-dir=./uploads
file.allowed-extensions=pdf,jpg,jpeg,png,doc,docx,xls,xlsx
```

### CORS Configuration
Update allowed origins:
```properties
cors.allowed-origins=http://localhost:4200
```

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/api/swagger-ui.html
```

API documentation (OpenAPI 3.0):
```
http://localhost:8080/api/v3/api-docs
```

## Key Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout

### File Management
- `POST /api/files/upload` - Upload file
- `GET /api/files/download/{fileName}` - Download file
- `DELETE /api/files/{fileName}` - Delete file

### Applications (to be implemented)
- `POST /api/applications` - Create application
- `GET /api/applications` - List applications
- `GET /api/applications/{id}` - Get application details
- `PUT /api/applications/{id}` - Update application
- `POST /api/applications/{id}/submit` - Submit application

### Workflow (to be implemented)
- `POST /api/applications/{id}/approve` - Approve application
- `POST /api/applications/{id}/return` - Return for correction
- `POST /api/applications/{id}/verify` - Assign verification
- `POST /api/applications/{id}/recommend` - NRCC recommendation
- `POST /api/applications/{id}/decision` - Minister decision

## User Roles

The system supports the following user roles:

- `PUBLIC_APPLICANT` - External applicants
- `MEMBER_OF_PARLIAMENT` - MPs submitting applications
- `REGIONAL_ROADS_BOARD_INITIATOR` - Regional board initiators
- `REGIONAL_ADMINISTRATIVE_SECRETARY` - RAS
- `REGIONAL_COMMISSIONER` - RC
- `MINISTER_OF_WORKS` - Minister
- `NRCC_CHAIRPERSON` - NRCC Chair
- `NRCC_MEMBER` - NRCC verification members
- `NRCC_SECRETARIAT` - NRCC secretariat
- `MINISTRY_LAWYER` - Legal officer
- `SYSTEM_ADMINISTRATOR` - System admin

## Security

### Authentication
The API uses JWT (JSON Web Tokens) for authentication:
1. Register or login to receive an access token
2. Include the token in subsequent requests:
```
Authorization: Bearer <your-token>
```

### Password Encryption
User passwords are encrypted using BCrypt before storage.

### HTTPS
In production, ensure all traffic uses HTTPS.

## Database Schema

The application automatically creates/updates database schema on startup (in dev mode). Key entities include:

- **User** - System users with roles
- **Application** - Road reclassification requests
- **ApplicationFormData** - Form data (Fourth Schedule)
- **EligibilityCriteriaSelection** - Selected eligibility criteria
- **ApprovalAction** - Workflow audit trail
- **VerificationAssignment** - NRCC verification tasks
- **VerificationReport** - Site verification reports
- **NRCCMeeting** - Meeting records
- **Recommendation** - NRCC recommendations
- **MinisterDecision** - Minister decisions
- **Gazettement** - Gazettement tracking
- **Appeal** - Appeal submissions
- **Notification** - Email/SMS/Portal notifications
- **ActionPlan** - Annual action plans
- **ActionPlanTarget** - Plan targets
- **ActionPlanActivity** - Plan activities
- **ActionPlanCostItem** - Budget items

## Development

### Adding New Features

1. **Create Entity** in `entity/` package
2. **Create Repository** interface in `repository/`
3. **Create DTOs** in `dto/request/` and `dto/response/`
4. **Implement Service** in `service/`
5. **Create Controller** in `controller/`
6. **Add Tests** in `src/test/`

### Code Style
- Use Lombok annotations to reduce boilerplate
- Follow REST conventions for endpoints
- Use `@Transactional` for service methods that modify data
- Implement proper exception handling

## Testing

Run tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=AuthServiceTest
```

## Production Deployment

### 1. Build Production JAR
```bash
mvn clean package -Pprod
```

### 2. Set Environment Variables
```bash
export SPRING_PROFILES_ACTIVE=prod
export DATABASE_URL=jdbc:postgresql://prod-db:5432/nrcc_prod_db
export DATABASE_USERNAME=prod_user
export DATABASE_PASSWORD=strong_password
export JWT_SECRET=your-strong-256-bit-secret
export MAIL_HOST=smtp.yourdomain.com
export MAIL_USERNAME=noreply@yourdomain.com
export MAIL_PASSWORD=mail_password
```

### 3. Run Application
```bash
java -jar target/nrcc-backend-1.0.0.jar
```

Or use Docker:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/nrcc-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Monitoring

### Actuator Endpoints
Access health and metrics:
- `GET /api/actuator/health` - Application health
- `GET /api/actuator/info` - Application info
- `GET /api/actuator/metrics` - Application metrics

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running
- Check database credentials
- Ensure database exists
- Check firewall/network settings

### JWT Issues
- Ensure JWT secret is at least 256 bits
- Check token expiration times
- Verify token is included in Authorization header

### File Upload Issues
- Check upload directory permissions
- Verify file size limits
- Ensure allowed extensions are configured

### Email Sending Issues
- Verify SMTP credentials
- Check firewall allows SMTP port
- For Gmail, use app-specific password

## Support

For issues and questions:
- **Project**: NRCC Database Management System
- **Organization**: Roads Fund Board - Tanzania
- **Version**: 1.0.0

## License

Copyright © 2025 Roads Fund Board, Tanzania

## Next Steps for Development

The current implementation provides the foundation. To complete the system:

1. **Implement Application Service & Controller**
   - Create/update/delete applications
   - Submit applications
   - Eligibility validation logic

2. **Implement Workflow Services**
   - RAS/RC approval workflows
   - NRCC verification assignment
   - Recommendation submission
   - Minister decision processing

3. **Implement Action Plan Services**
   - Create/update action plans
   - Budget calculation
   - Progress tracking

4. **Implement Reporting Services**
   - Application reports
   - Status reports
   - Budget reports

5. **Add Audit Logging**
   - Track all changes
   - Generate audit reports

6. **Implement Additional Features**
   - Password reset
   - Email verification
   - User management
   - Dashboard statistics

## Example: Creating Your First Application

1. **Register a user**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "PUBLIC_APPLICANT"
  }'
```

2. **Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

3. **Use the token** for subsequent requests.

---

**Built with Spring Boot for the Roads Fund Board, Tanzania**
