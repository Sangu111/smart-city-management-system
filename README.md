# Smart City Management System – Full Stack Java Web Application

## Live Demo
Live Link: Smart City Management System on Render (example link)  
Demo Credentials: https://smart-city-management-system-nnev.onrender.com  
- Admin: admin / admin123  
- Citizen: citizen1 / test123

## Project Overview
The Smart City Management System is a full-stack Java web application designed to streamline complaint management for citizens and administrators. It provides a secure platform for registering complaints, tracking their status, and managing city governance efficiently.

## Key Highlights
- Enterprise-grade Java EE + JSP + Servlets + JDBC + MySQL stack
- Secure authentication with SHA-256 password hashing
- Role-based dashboards for Admin & Citizens
- Complaint workflow with live status tracking
- Responsive UI with modern CSS3 and professional UX
- Clean MVC Architecture with DAO layer separation

## Tech Stack
- Backend: Java EE, JSP, Servlets, JDBC
- Frontend: HTML5, CSS3, JSP
- Database: MySQL 8.0
- Server: Apache Tomcat 9.0
- Build Tool: Maven
- Deployment: Docker + Render

## Database Design

### Entity Relationship Diagram

```
+---------------------+        1:N        +----------------------+
|       USERS         |------------------>|      COMPLAINTS      |
+---------------------+                   +----------------------+
| id (PK)             |                   | id (PK)              |
| username (UK)       |                   | user_id (FK)         |
| email (UK)          |                   | category             |
| password_hash       |                   | title                |
| role                |                   | description          |
| created_at          |                   | location             |
+---------------------+                   | photo_url            |
                                          | status               |
                                          | assigned_to          |
                                          | created_at           |
                                          | updated_at           |
                                          +----------------------+
```

### Database Schema

```sql
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(64) NOT NULL,
  role ENUM('citizen', 'admin') DEFAULT 'citizen',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE complaints (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  category VARCHAR(50) NOT NULL,
  title VARCHAR(200) NOT NULL,
  description TEXT NOT NULL,
  location VARCHAR(200) NOT NULL,
  status ENUM('Submitted','In Progress','Resolved','Rejected') DEFAULT 'Submitted',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Quick Start

### 1. Clone Repo
```sh
git clone https://github.com/your-username/SmartCityManagementSystem.git
cd SmartCityManagementSystem
```
### 2. Database Setup
```sh
CREATE DATABASE smart_city;
USE smart_city;
SOURCE sql/smart_city.sql;
```
### 3. Run Locally
Visit: http://localhost:8080/smart-city/

## Docker & Deployment
```sh
docker build -t smart-city .
docker run -p 8080:8080 \
-e JDBC_DATABASE_URL=jdbc:mysql://host:3306/smart_city \
-e JDBC_DATABASE_USER=root \
-e JDBC_DATABASE_PASSWORD=yourpassword \
smart-city
```

## Skills Demonstrated
- Enterprise Java Development (Java EE, Servlets, JDBC)
- Database Design & SQL optimization
- Web Development (JSP, HTML5, CSS3)
- Security Implementation (Auth, Role-based Access)
- Software Architecture (MVC, DAO Layered Design)
- Deployment (Docker, Render, CI/CD readiness)

## License
This project is for educational and demonstration purposes. © 2025 Smart City Management System
