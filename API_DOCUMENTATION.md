# 📋 API Documentation

## Overview
This document provides comprehensive API documentation for the Smart City Management System, designed for enterprise integration and third-party development.

## Base URL
```
http://localhost:8080/smart-city
```

## Authentication
All protected endpoints require valid session authentication. Users must login through the web interface to establish a session.

## Endpoints

### 🔐 Authentication Endpoints

#### POST /login
Authenticate user and create session.

**Request:**
```http
POST /login HTTP/1.1
Content-Type: application/x-www-form-urlencoded

username=your_username&password=your_password
```

**Response (Success):**
```http
HTTP/1.1 302 Found
Location: /dashboard
Set-Cookie: JSESSIONID=ABC123; Path=/; HttpOnly
```

**Response (Error):**
```http
HTTP/1.1 302 Found
Location: /jsp/login.jsp?error=1
```

#### POST /register
Register new user account.

**Request:**
```http
POST /register HTTP/1.1
Content-Type: application/x-www-form-urlencoded

username=newuser&email=user@example.com&password=secure_password&confirmPassword=secure_password
```

**Response (Success):**
```http
HTTP/1.1 302 Found
Location: /jsp/login.jsp?msg=registered
```

#### POST /logout
Terminate user session.

**Request:**
```http
POST /logout HTTP/1.1
Cookie: JSESSIONID=ABC123
```

**Response:**
```http
HTTP/1.1 302 Found
Location: /jsp/login.jsp
```

### 📋 Complaint Management Endpoints

#### POST /complaint
Submit new complaint (Citizens only).

**Request:**
```http
POST /complaint HTTP/1.1
Content-Type: multipart/form-data
Cookie: JSESSIONID=ABC123

action=submit
title=Pothole on Main Street
description=Large pothole causing traffic issues
category=Road
location=Main Street near City Hall
photo=[binary file data]
```

**Response (Success):**
```http
HTTP/1.1 302 Found
Location: /jsp/dashboard.jsp?msg=success
```

#### POST /admin
Admin complaint management operations.

**Update Complaint Status:**
```http
POST /admin HTTP/1.1
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=ABC123

action=updateStatus
complaintId=123
status=In Progress
assignedTo=Public Works Department
```

**Response:**
```http
HTTP/1.1 302 Found
Location: /jsp/admin_dashboard.jsp?msg=updated
```

### 📊 Dashboard Endpoints

#### GET /jsp/dashboard.jsp
Citizen dashboard (requires citizen authentication).

**Request:**
```http
GET /jsp/dashboard.jsp HTTP/1.1
Cookie: JSESSIONID=ABC123
```

**Response:**
```html
HTTP/1.1 200 OK
Content-Type: text/html

[HTML content with user complaints and status]
```

#### GET /jsp/admin_dashboard.jsp
Admin dashboard (requires admin authentication).

**Request:**
```http
GET /jsp/admin_dashboard.jsp HTTP/1.1
Cookie: JSESSIONID=ABC123
```

**Response:**
```html
HTTP/1.1 200 OK
Content-Type: text/html

[HTML content with all complaints management interface]
```

## Data Models

### User Model
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@smartcity.com",
  "role": "admin",
  "created_at": "2025-09-28T10:30:00Z"
}
```

### Complaint Model
```json
{
  "id": 1,
  "user_id": 2,
  "category": "Road",
  "title": "Pothole on Main Street",
  "description": "Large pothole causing traffic issues",
  "location": "Main Street near City Hall",
  "photo_url": "/uploads/complaint_1_photo.jpg",
  "status": "Submitted",
  "assigned_to": null,
  "created_at": "2025-09-28T10:30:00Z",
  "updated_at": "2025-09-28T10:30:00Z"
}
```

## Status Codes

### HTTP Status Codes
- **200 OK**: Request successful
- **302 Found**: Redirect (common for form submissions)
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Access denied (role-based)
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

### Application Status Codes

#### Complaint Status Values
- **Submitted**: Initial complaint submission
- **In Progress**: Complaint being processed
- **Resolved**: Complaint successfully resolved
- **Rejected**: Complaint rejected or invalid

#### User Roles
- **citizen**: Regular user with complaint submission rights
- **admin**: Administrative user with full system access

## Error Handling

### Error Response Format
Errors are handled through URL parameters in redirects:

**Authentication Error:**
```
/jsp/login.jsp?error=1
```

**Registration Errors:**
```
/jsp/register.jsp?error=username_exists
/jsp/register.jsp?error=password_mismatch
/jsp/register.jsp?error=database_error
```

**Success Messages:**
```
/jsp/login.jsp?msg=registered
/jsp/dashboard.jsp?msg=success
/jsp/admin_dashboard.jsp?msg=updated
```

## File Upload

### Supported File Types
- **Images**: JPG, JPEG, PNG, GIF
- **Maximum Size**: 10MB per file
- **Storage Location**: `/uploads/` directory

### Upload Request Format
```http
POST /complaint HTTP/1.1
Content-Type: multipart/form-data
Content-Length: [size]

--boundary123
Content-Disposition: form-data; name="photo"; filename="pothole.jpg"
Content-Type: image/jpeg

[binary file data]
--boundary123--
```

## Security Considerations

### Authentication
- Session-based authentication using JSESSIONID
- Password hashing with SHA-256
- Session timeout after inactivity

### Input Validation
- All form inputs validated server-side
- SQL injection prevention through prepared statements
- File upload validation for type and size

### Access Control
- Role-based access control (RBAC)
- Admin endpoints restricted to admin users
- User isolation (citizens can only see their own complaints)

## Rate Limiting
Currently no rate limiting implemented. For production deployment, consider:
- Request throttling per IP address
- User-based request limits
- API key authentication for programmatic access

## Integration Examples

### JavaScript Client Example
```javascript
// Login request
async function login(username, password) {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('password', password);
    
    const response = await fetch('/smart-city/login', {
        method: 'POST',
        body: formData
    });
    
    return response.redirected;
}

// Submit complaint
async function submitComplaint(complaintData, photoFile) {
    const formData = new FormData();
    formData.append('action', 'submit');
    formData.append('title', complaintData.title);
    formData.append('description', complaintData.description);
    formData.append('category', complaintData.category);
    formData.append('location', complaintData.location);
    
    if (photoFile) {
        formData.append('photo', photoFile);
    }
    
    const response = await fetch('/smart-city/complaint', {
        method: 'POST',
        body: formData
    });
    
    return response.ok;
}
```

### cURL Examples
```bash
# Login
curl -X POST http://localhost:8080/smart-city/login \
  -d "username=your_username&password=your_password" \
  -c cookies.txt

# Submit complaint
curl -X POST http://localhost:8080/smart-city/complaint \
  -b cookies.txt \
  -F "action=submit" \
  -F "title=Street Light Not Working" \
  -F "description=Street light has been out for 3 days" \
  -F "category=Electricity" \
  -F "location=Park Avenue Block 2" \
  -F "photo=@streetlight.jpg"

# Update complaint status (admin only)
curl -X POST http://localhost:8080/smart-city/admin \
  -b cookies.txt \
  -d "action=updateStatus&complaintId=123&status=Resolved"
```

## Testing the API

### Manual Testing Steps
1. **Setup**: Ensure application is running on Tomcat
2. **Database**: Verify MySQL database is populated with test data
3. **Authentication**: Test login with configured credentials
4. **Citizen Actions**: Test complaint submission
5. **Admin Actions**: Test complaint status updates
6. **Logout**: Verify session termination

### Automated Testing
For automated API testing, consider tools like:
- **Postman**: GUI-based API testing
- **Newman**: Command-line Postman runner
- **JUnit**: Java-based integration tests
- **Selenium**: End-to-end web testing

This API documentation provides a comprehensive guide for developers integrating with or extending the Smart City Management System.