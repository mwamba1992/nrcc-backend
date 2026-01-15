# User Management & Role-Based Access Control

## Overview

The NRCC system implements a comprehensive Role-Based Access Control (RBAC) system with fine-grained permissions. This document describes the user management architecture and how to use it.

## Architecture

### Components

1. **Roles** (`UserRole` enum) - High-level user categories
2. **Permissions** (`Permission` enum) - Fine-grained action permissions
3. **RolePermissionConfig** - Maps roles to their allowed permissions
4. **PermissionChecker** - Runtime permission validation
5. **@RequirePermission** - Annotation for method-level security
6. **PermissionAspect** - AOP interceptor for permission enforcement

## User Roles

The system supports 11 distinct user roles:

### 1. PUBLIC_APPLICANT
External users submitting applications
- Can create and manage own applications
- Can submit and appeal applications
- Upload/download files related to their applications

### 2. MEMBER_OF_PARLIAMENT
MPs submitting applications on behalf of constituents
- Same as PUBLIC_APPLICANT plus:
- Can view application lists
- Can view reports

### 3. REGIONAL_ROADS_BOARD_INITIATOR
Regional board officials initiating applications
- Can create and manage applications
- Can view application lists and reports

### 4. REGIONAL_ADMINISTRATIVE_SECRETARY (RAS)
Reviews applications from regional boards
- Can approve or return applications
- Can view applications and reports

### 5. REGIONAL_COMMISSIONER (RC)
Final regional approval level
- Can approve or return applications
- Can view applications and reports

### 6. MINISTER_OF_WORKS
Final decision authority
- Can make final decisions on applications
- Can approve applications
- Full reporting access with export capabilities

### 7. NRCC_CHAIRPERSON
Leads the verification committee
- Can assign verification tasks
- Can create recommendations
- Can manage action plans
- Full reporting capabilities

### 8. NRCC_MEMBER
Verification team members
- Can conduct site verifications
- Can submit verification reports
- Can track action plans

### 9. NRCC_SECRETARIAT
Administrative support
- Can update applications
- Can send notifications
- Can manage action plans
- Full reporting access

### 10. MINISTRY_LAWYER
Legal officer for gazettement
- Can update gazettement status
- Can view applications and reports

### 11. SYSTEM_ADMINISTRATOR
Full system access
- All permissions granted
- User management
- System configuration
- Audit log access

## Permissions

### User Management Permissions
- `USER_CREATE` - Create new users
- `USER_READ` - View user details
- `USER_UPDATE` - Modify user information
- `USER_DELETE` - Delete users
- `USER_LIST` - View user lists

### Application Management Permissions
- `APPLICATION_CREATE` - Create applications
- `APPLICATION_READ` - View applications
- `APPLICATION_UPDATE` - Modify applications
- `APPLICATION_DELETE` - Delete applications
- `APPLICATION_SUBMIT` - Submit applications
- `APPLICATION_LIST` - View application lists

### Workflow Permissions
- `APPLICATION_APPROVE` - Approve applications
- `APPLICATION_RETURN` - Return for correction
- `APPLICATION_ASSIGN_VERIFICATION` - Assign verifications
- `APPLICATION_VERIFY` - Conduct verifications
- `APPLICATION_RECOMMEND` - Create recommendations
- `APPLICATION_DECIDE` - Make final decisions
- `APPLICATION_GAZETTE` - Update gazettement
- `APPLICATION_APPEAL` - Submit appeals

### Action Plan Permissions
- `ACTION_PLAN_CREATE` - Create action plans
- `ACTION_PLAN_READ` - View action plans
- `ACTION_PLAN_UPDATE` - Modify action plans
- `ACTION_PLAN_DELETE` - Delete action plans
- `ACTION_PLAN_APPROVE` - Approve action plans
- `ACTION_PLAN_TRACK` - Track plan progress

### Reporting Permissions
- `REPORT_VIEW` - View reports
- `REPORT_EXPORT` - Export reports
- `REPORT_STATISTICS` - View statistics

### System Permissions
- `SYSTEM_CONFIGURE` - System configuration
- `AUDIT_LOG_VIEW` - View audit logs
- `NOTIFICATION_SEND` - Send notifications

### File Management Permissions
- `FILE_UPLOAD` - Upload files
- `FILE_DOWNLOAD` - Download files
- `FILE_DELETE` - Delete files

## API Endpoints

### Base URL: `/api/users`

### Create User
```http
POST /api/users
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phoneNumber": "+255712345678",
  "role": "PUBLIC_APPLICANT",
  "organization": "Organization Name",
  "region": "Dodoma"
}
```

**Required Permission:** `USER_CREATE`

**Response:**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "phoneNumber": "+255712345678",
    "role": "PUBLIC_APPLICANT",
    "organization": "Organization Name",
    "region": "Dodoma",
    "status": "ACTIVE",
    "emailVerified": false,
    "phoneVerified": false,
    "lastLogin": null,
    "createdAt": "2025-01-15T10:00:00",
    "updatedAt": "2025-01-15T10:00:00",
    "permissions": [
      "APPLICATION_APPEAL",
      "APPLICATION_CREATE",
      "APPLICATION_READ",
      "APPLICATION_SUBMIT",
      "APPLICATION_UPDATE",
      "FILE_DOWNLOAD",
      "FILE_UPLOAD"
    ]
  }
}
```

### Get User by ID
```http
GET /api/users/{id}
Authorization: Bearer <token>
```

**Required Permission:** `USER_READ` or `USER_LIST`

### Get All Users
```http
GET /api/users
Authorization: Bearer <token>
```

**Required Permission:** `USER_LIST`

### Get Users with Pagination
```http
GET /api/users/paginated?page=0&size=10&sortBy=name&sortDirection=ASC
Authorization: Bearer <token>
```

**Required Permission:** `USER_LIST`

**Query Parameters:**
- `page` - Page number (default: 0)
- `size` - Page size (default: 10)
- `sortBy` - Sort field (default: id)
- `sortDirection` - ASC or DESC (default: ASC)

### Get Users by Role
```http
GET /api/users/role/PUBLIC_APPLICANT
Authorization: Bearer <token>
```

**Required Permission:** `USER_LIST`

### Get Users by Region
```http
GET /api/users/region/Dodoma
Authorization: Bearer <token>
```

**Required Permission:** `USER_LIST`

### Get Users by Status
```http
GET /api/users/status/ACTIVE
Authorization: Bearer <token>
```

**Required Permission:** `USER_LIST`

### Update User
```http
PUT /api/users/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "John Updated",
  "phoneNumber": "+255712345679",
  "status": "ACTIVE"
}
```

**Required Permission:** `USER_UPDATE`

### Change Password
```http
PUT /api/users/{id}/change-password
Authorization: Bearer <token>
Content-Type: application/json

{
  "currentPassword": "oldPassword123",
  "newPassword": "newPassword123",
  "confirmPassword": "newPassword123"
}
```

**No specific permission required** (users can change their own password)

### Delete User
```http
DELETE /api/users/{id}
Authorization: Bearer <token>
```

**Required Permission:** `USER_DELETE`

### Activate User
```http
PUT /api/users/{id}/activate
Authorization: Bearer <token>
```

**Required Permission:** `USER_UPDATE`

### Deactivate User
```http
PUT /api/users/{id}/deactivate
Authorization: Bearer <token>
```

**Required Permission:** `USER_UPDATE`

## Using Permissions in Code

### Method-Level Permission Checking

Use the `@RequirePermission` annotation on controller methods:

```java
@GetMapping("/applications")
@RequirePermission(Permission.APPLICATION_LIST)
public ResponseEntity<List<Application>> getApplications() {
    // Only users with APPLICATION_LIST permission can access
}
```

**Require ALL permissions:**
```java
@PostMapping("/applications/{id}/approve")
@RequirePermission({Permission.APPLICATION_READ, Permission.APPLICATION_APPROVE})
public ResponseEntity<?> approveApplication(@PathVariable Long id) {
    // User must have BOTH permissions
}
```

**Require ANY permission:**
```java
@GetMapping("/applications/{id}")
@RequirePermission(anyOf = {Permission.APPLICATION_READ, Permission.APPLICATION_UPDATE})
public ResponseEntity<?> getApplication(@PathVariable Long id) {
    // User must have AT LEAST ONE permission
}
```

### Programmatic Permission Checking

Inject `PermissionChecker` into your service:

```java
@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final PermissionChecker permissionChecker;

    public void processApplication(Long id) {
        if (!permissionChecker.hasPermission(Permission.APPLICATION_APPROVE)) {
            throw new UnauthorizedException("Insufficient permissions");
        }

        // Process application
    }

    public void multiplePermissions() {
        // Check for ANY permission
        if (permissionChecker.hasAnyPermission(
            Permission.APPLICATION_READ,
            Permission.APPLICATION_UPDATE)) {
            // User has at least one
        }

        // Check for ALL permissions
        if (permissionChecker.hasAllPermissions(
            Permission.APPLICATION_READ,
            Permission.APPLICATION_APPROVE)) {
            // User has both
        }
    }
}
```

### Get Current User Information

Use `SecurityUtil` to get current user details:

```java
import tz.go.roadsfund.nrcc.util.SecurityUtil;

public class MyService {

    public void someMethod() {
        Long userId = SecurityUtil.getCurrentUserId();
        String email = SecurityUtil.getCurrentUserEmail();
        String role = SecurityUtil.getCurrentUserRole();

        if (SecurityUtil.isAuthenticated()) {
            // User is logged in
        }
    }
}
```

## Role-Permission Matrix

| Permission | Admin | Minister | NRCC Chair | NRCC Member | RAS | RC | Regional Board | MP | Public |
|------------|-------|----------|------------|-------------|-----|----|--------------|----|--------|
| USER_CREATE | ✓ | | | | | | | | |
| USER_READ | ✓ | | | | | | | | |
| USER_UPDATE | ✓ | | | | | | | | |
| USER_DELETE | ✓ | | | | | | | | |
| APPLICATION_CREATE | ✓ | | | | | | ✓ | ✓ | ✓ |
| APPLICATION_READ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ | ✓ |
| APPLICATION_APPROVE | ✓ | ✓ | | | ✓ | ✓ | | | |
| APPLICATION_DECIDE | ✓ | ✓ | | | | | | | |
| APPLICATION_VERIFY | ✓ | | | ✓ | | | | | |
| APPLICATION_RECOMMEND | ✓ | | ✓ | | | | | | |
| APPLICATION_GAZETTE | ✓ | | | | | | | | |
| REPORT_VIEW | ✓ | ✓ | ✓ | | ✓ | ✓ | ✓ | ✓ | |
| REPORT_EXPORT | ✓ | ✓ | ✓ | | | | | | |
| SYSTEM_CONFIGURE | ✓ | | | | | | | | |

## Security Best Practices

### 1. Always Use Permissions
Never check roles directly. Always check permissions:

```java
// BAD - Don't do this
if (user.getRole().equals(UserRole.SYSTEM_ADMINISTRATOR)) {
    // ...
}

// GOOD - Do this
if (permissionChecker.hasPermission(Permission.SYSTEM_CONFIGURE)) {
    // ...
}
```

### 2. Apply Least Privilege
Grant only the permissions needed for a user's job function.

### 3. Use Method-Level Security
Prefer `@RequirePermission` annotation over programmatic checks when possible.

### 4. Audit User Actions
All user creation, updates, and deletions are logged automatically.

### 5. Strong Passwords
Passwords are hashed using BCrypt with work factor 10.

### 6. Account Status
Use `ACTIVE`, `INACTIVE`, `SUSPENDED`, `LOCKED` statuses to control access.

## Testing Permissions

### Example: Testing User Management

```bash
# 1. Login as admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nrcc.go.tz",
    "password": "password123"
  }'

# Save the token
TOKEN="<your-token>"

# 2. Create a new user
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "test123",
    "role": "PUBLIC_APPLICANT"
  }'

# 3. List all users
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN"

# 4. Get users by role
curl -X GET http://localhost:8080/api/users/role/PUBLIC_APPLICANT \
  -H "Authorization: Bearer $TOKEN"

# 5. Update user
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Name",
    "status": "ACTIVE"
  }'

# 6. Deactivate user
curl -X PUT http://localhost:8080/api/users/1/deactivate \
  -H "Authorization: Bearer $TOKEN"
```

## Troubleshooting

### Permission Denied Errors

**Error:** `"Insufficient permissions to perform this action"`

**Solution:**
1. Check the user's role in the database
2. Verify the role has the required permission in `RolePermissionConfig`
3. Check if the user's account status is ACTIVE

### User Can't Login

**Possible causes:**
1. Account status is not ACTIVE
2. Password is incorrect
3. Email is not verified (if email verification is enforced)

**Solution:**
- Check user status: `SELECT status FROM users WHERE email = 'user@example.com'`
- Activate user: `UPDATE users SET status = 'ACTIVE' WHERE email = 'user@example.com'`

## Future Enhancements

Potential improvements for the permission system:

1. **Custom Permissions** - Allow creating custom permissions per deployment
2. **Permission Groups** - Group related permissions for easier management
3. **Temporary Permissions** - Grant time-limited permissions
4. **Permission Delegation** - Allow users to delegate specific permissions
5. **Audit Trail** - Enhanced audit logging for permission changes
6. **Multi-tenancy** - Support for multiple organizations

---

**For more information, see:**
- `RolePermissionConfig.java` - Role-permission mappings
- `PermissionChecker.java` - Permission validation logic
- `UserController.java` - User management API
- `SecurityUtil.java` - Security utilities
