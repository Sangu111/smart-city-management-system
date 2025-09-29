# 🏙️ smart-city-management-system

**Live Application**: https://smart-city-management-system-nnev.onrender.com

| Demo Credentials | Username | Password |
|------------------|----------|----------|
| 👨‍💼 **Administrator** | `admin` | `admin123` |
| 👤 **Citizen User** | `citizen1` | `test123` |management-system

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Tomcat](https://img.shields.io/badge/Apache%20Tomcat-9.0+-D22128?style=for-the-badge&logo=apache-tomcat&logoColor=white)](https://tomcat.apache.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

> **Enterprise-grade Java web application for urban infrastructure management, built with modern Java EE technologies and security-first approach.**

## � **Quick Demo Access**

**Live Application**: http://localhost:8080/smart-city/

| Demo Credentials | Username | Password |
|------------------|----------|----------|
| 👨‍💼 **Administrator** | `admin` | `admin123` |
| 👤 **Citizen User** | `citizen1` | `test123` |

*Ready for immediate testing and evaluation!*

---

## �📋 Table of Contents

- [🎯 Project Overview](#-project-overview)
- [🏗️ System Architecture](#️-system-architecture)
- [💼 Technology Stack](#-technology-stack)
- [🗄️ Database Design](#️-database-design)
- [🚀 Features](#-features)
- [🔒 Security Implementation](#-security-implementation)
- [⚡ Quick Start Guide](#-quick-start-guide)
- [📁 Project Structure](#-project-structure)
- [🧪 Testing & Validation](#-testing--validation)
- [📊 Performance & Scalability](#-performance--scalability)
- [🎨 UI/UX Design](#-uiux-design)
- [📈 Future Enhancements](#-future-enhancements)

## 🎯 Project Overview

The **Smart City Management System** is a comprehensive web-based platform designed to streamline urban infrastructure management and citizen services. Built using enterprise Java technologies, it provides a robust solution for handling citizen complaints, tracking infrastructure issues, and enabling efficient municipal administration.

### 🎪 Key Highlights
- **Full-Stack Enterprise Application** with MVC architecture
- **Role-Based Authentication** with secure session management
- **Professional UI/UX** with responsive design
- **Security-First Development** with industry best practices
- **Scalable Database Design** with normalized schema
- **RESTful Architecture** with clean separation of concerns

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
├─────────────────────────────────────────────────────────────┤
│  JSP Pages │ CSS/JavaScript │ FontAwesome │ Responsive UI   │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                   BUSINESS LOGIC LAYER                      │
├─────────────────────────────────────────────────────────────┤
│ Login │ Register │ Complaint │ Admin │ Logout │ Validation  │
│     Servlet Controllers & Session Management                │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                   DATA ACCESS LAYER                         │
├─────────────────────────────────────────────────────────────┤
│ UserDAO │ ComplaintDAO │ DBUtil │ PasswordUtil │ Connection │
│           Pool │ Prepared Statements │ Security              │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────▼───────────────────────────────────┐
│                     DATABASE LAYER                          │
├─────────────────────────────────────────────────────────────┤
│         MySQL Database │ Users Table │ Complaints Table     │
└─────────────────────────────────────────────────────────────┘
```

### 🔧 Architecture Patterns Used
- **Model-View-Controller (MVC)** - Clean separation of concerns
- **Data Access Object (DAO)** - Abstracted database operations
- **Front Controller** - Centralized request handling
- **Session Façade** - Simplified client interface
- **Dependency Injection** - Loose coupling between components

## 💼 Technology Stack

### Backend Technologies
| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17+ | Core application logic |
| **Java EE Servlets** | 4.0 | HTTP request handling |
| **JDBC** | Latest | Database connectivity |
| **Maven** | 3.9.11 | Build automation & dependency management |

### Frontend Technologies
| Technology | Version | Purpose |
|------------|---------|---------|
| **JSP** | 2.3 | Server-side rendering |
| **HTML5** | Latest | Semantic markup |
| **CSS3** | Latest | Modern styling & animations |
| **JavaScript** | ES6+ | Client-side interactivity |
| **FontAwesome** | 6.0+ | Professional iconography |

### Infrastructure
| Component | Version | Purpose |
|-----------|---------|---------|
| **Apache Tomcat** | 9.0+ | Java servlet container |
| **MySQL** | 8.0+ | Relational database |
| **Git** | Latest | Version control |

## 🗄️ Database Design

### Entity Relationship Diagram

```
    ┌─────────────────────┐           ┌─────────────────────┐
    │       USERS         │           │    COMPLAINTS       │
    ├─────────────────────┤           ├─────────────────────┤
    │ id (PK)            │           │ id (PK)            │
    │ username (UK)      │           │ user_id (FK)       │
    │ email (UK)         │◄──────────┤ category           │
    │ password_hash      │    1:N    │ title              │
    │ role               │           │ description        │
    │ created_at         │           │ location           │
    └─────────────────────┘           │ photo_url          │
                                      │ status             │
                                      │ assigned_to        │
                                      │ created_at         │
                                      │ updated_at         │
                                      └─────────────────────┘
```

### Database Schema Details

#### Users Table
```sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(64) NOT NULL,
    role ENUM('citizen', 'admin') DEFAULT 'citizen',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### Complaints Table
```sql
CREATE TABLE complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(200) NOT NULL,
    photo_url VARCHAR(255),
    status ENUM('Submitted', 'In Progress', 'Resolved', 'Rejected') DEFAULT 'Submitted',
    assigned_to VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Data Normalization
- **1NF**: All attributes contain atomic values
- **2NF**: No partial dependencies on composite keys
- **3NF**: No transitive dependencies
- **Referential Integrity**: Foreign key constraints implemented

## 🚀 Features

### 👥 Citizen Portal
- **User Registration & Authentication**
  - Secure account creation with email validation
  - SHA-256 password hashing
  - Session-based authentication
  
- **Complaint Management**
  - Submit complaints with multiple categories (Road, Water, Electricity, Sanitation)
  - Upload supporting photographs
  - Real-time status tracking
  - Complaint history with filtering
  
- **Dashboard & Analytics**
  - Personal complaint dashboard
  - Status-wise complaint analytics
  - Location-based complaint mapping

### 👨‍💼 Administrative Panel
- **Admin Dashboard**
  - System-wide complaint overview
  - Statistical insights and reporting
  - Priority-based complaint sorting
  
- **Complaint Operations**
  - View all citizen complaints
  - Update complaint status workflow
  - Assign complaints to departments
  - Bulk operations support
  
- **User Management**
  - View registered citizens
  - Account status management
  - Role-based access control

### 🔄 Workflow Management
```
    [Submitted] ──→ [In Progress] ──→ [Resolved]
         │                │              │
         │                ▼              ▼
         └──────────→ [Rejected] ────→ [Closed]
```

## 🔒 Security Implementation

### Authentication & Authorization
- **Password Security**: SHA-256 hashing with salting
- **Session Management**: Secure HTTP sessions with timeout
- **Role-Based Access Control**: Admin/Citizen permission levels
- **Input Validation**: Server-side validation for all forms

### Data Protection
- **SQL Injection Prevention**: Prepared statements throughout
- **XSS Protection**: Input sanitization and output encoding
- **CSRF Protection**: Token-based form validation
- **File Upload Security**: Type validation and size limits

### Security Headers
```java
// Implemented security measures
response.setHeader("X-Content-Type-Options", "nosniff");
response.setHeader("X-Frame-Options", "DENY");
response.setHeader("X-XSS-Protection", "1; mode=block");
```

## ⚡ Quick Start Guide

### Prerequisites
```bash
# Required software versions
Java 17+
Apache Tomcat 9.0+
MySQL 8.0+
Maven 3.9.11+
Git
```

### Installation Steps

1. **Clone Repository**
```bash
git clone https://github.com/yourusername/smart-city-management.git
cd smart-city-management
```

2. **Database Setup**
```bash
# Create database and tables
mysql -u root -p < sql/smartcity.sql

# Verify database creation
mysql -u root -p -e "USE smartcity; SHOW TABLES;"
```

3. **Configuration**
```bash
# Update database credentials in src/main/resources/db.properties
db.url=jdbc:mysql://localhost:3306/smartcity?useSSL=false&serverTimezone=UTC
db.username=your_username
db.password=your_password
db.driver=com.mysql.cj.jdbc.Driver
```

4. **Build Application**
```bash
# Clean and compile
mvn clean compile

# Create WAR file
mvn package

# Verify build
ls -la target/smart-city.war
```

5. **Deploy to Tomcat**
```bash
# Copy WAR to Tomcat webapps
cp target/smart-city.war $TOMCAT_HOME/webapps/

# Start Tomcat server
$TOMCAT_HOME/bin/startup.sh  # Linux/Mac
$TOMCAT_HOME/bin/startup.bat # Windows
```

6. **Access Application**
```
Local: http://localhost:8080/smart-city/
Live Demo: https://smart-city-management-system-nnev.onrender.com
```

### 🎪 Demo Credentials for Testing

| Role | Username | Password | Access Level |
|------|----------|----------|-------------|
| **Administrator** | `admin` | `admin123` | Full system access, complaint management, user oversight |
| **Citizen** | `citizen1` | `test123` | Submit complaints, view personal dashboard |

> **For Microsoft Recruiters**: These demo accounts provide complete access to all application features for comprehensive evaluation.

## � Docker Deployment

### Quick Start with Docker

1. **Prerequisites**
   - Docker Desktop installed
   - Docker Compose available

2. **One-Command Deployment**
```bash
# For Linux/Mac
chmod +x deploy.sh
./deploy.sh

# For Windows PowerShell
.\deploy.ps1
```

3. **Manual Docker Commands**
```bash
# Build and run with database
docker-compose up --build -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f

# Stop containers
docker-compose down
```

4. **Environment Variables for Production**
```bash
export JDBC_DATABASE_URL="your-database-url"
export JDBC_DATABASE_USER="your-username"
export JDBC_DATABASE_PASSWORD="your-password"

# Run with custom DB
docker run -p 8080:8080 \
  -e JDBC_DATABASE_URL="$JDBC_DATABASE_URL" \
  -e JDBC_DATABASE_USER="$JDBC_DATABASE_USER" \
  -e JDBC_DATABASE_PASSWORD="$JDBC_DATABASE_PASSWORD" \
  smart-city-app
```

### 🌐 Render Deployment

1. **Connect Repository**
   - Fork/clone this repository
   - Connect to Render from GitHub

2. **Environment Variables on Render**
   ```
   JDBC_DATABASE_URL=your-mysql-url
   JDBC_DATABASE_USER=your-db-username  
   JDBC_DATABASE_PASSWORD=your-db-password
   ```

3. **Render Configuration**
   - **Build Command**: `docker build -t smart-city .`
   - **Start Command**: `docker run -p $PORT:8080 smart-city`
   - **Environment**: Docker

## �📁 Project Structure

```
smart-city-management/
│
├── 📁 src/main/
│   ├── 📁 java/com/smartcity/
│   │   ├── 📁 dao/                    # Data Access Layer
│   │   │   ├── 📄 DBUtil.java         # Database connection utility
│   │   │   ├── 📄 UserDAO.java        # User data operations
│   │   │   ├── 📄 ComplaintDAO.java   # Complaint data operations
│   │   │   └── 📄 PasswordUtil.java   # Password hashing utility
│   │   │
│   │   ├── 📁 model/                  # Entity Classes
│   │   │   ├── 📄 User.java           # User entity
│   │   │   └── 📄 Complaint.java      # Complaint entity
│   │   │
│   │   ├── 📁 servlet/                # HTTP Request Handlers
│   │   │   ├── 📄 LoginServlet.java   # Authentication handler
│   │   │   ├── 📄 RegisterServlet.java# Registration handler
│   │   │   ├── 📄 ComplaintServlet.java# Complaint operations
│   │   │   ├── 📄 AdminServlet.java   # Admin operations
│   │   │   └── 📄 LogoutServlet.java  # Session termination
│   │   │
│   │   └── 📁 util/                   # Utility Classes
│   │       └── 📄 HashGenerator.java  # Password hash generator
│   │
│   ├── 📁 resources/
│   │   └── 📄 db.properties           # Database configuration
│   │
│   └── 📁 webapp/                     # Web Content
│       ├── 📄 index.html              # Landing page
│       ├── 📁 jsp/                    # JSP Templates
│       │   ├── 📄 login.jsp           # Login interface
│       │   ├── 📄 register.jsp        # Registration form
│       │   ├── 📄 dashboard.jsp       # Citizen dashboard
│       │   ├── 📄 admin_dashboard.jsp # Admin interface
│       │   └── 📄 complaint_form.jsp  # Complaint submission
│       ├── 📁 uploads/                # File upload directory
│       └── 📁 WEB-INF/
│           └── 📄 web.xml             # Servlet configuration
│
├── 📁 sql/
│   └── 📄 smartcity.sql               # Database schema & sample data
│
├── 📁 target/                         # Build Output
│   └── 📄 smart-city.war              # Deployable WAR file
│
├── 📄 pom.xml                         # Maven configuration
├── 📄 README.md                       # Project documentation
├── 📄 SECURITY.md                     # Security guidelines
├── 📄 TESTING_GUIDE.md               # Testing procedures
└── 📄 .gitignore                      # Git ignore rules
```

## 🧪 Testing & Validation

### Manual Testing Checklist
- User registration with validation
- Login/logout functionality  
- Complaint submission with file upload
- Admin complaint management
- Status update workflows
- Security validation (SQL injection, XSS)
- Mobile responsiveness
- Cross-browser compatibility

### Performance Testing
- **Load Testing**: Concurrent user simulation
- **Database Performance**: Query optimization analysis
- **Memory Management**: JVM heap monitoring
- **Response Times**: Page load performance metrics

## 📊 Performance & Scalability

### Optimization Techniques
- **Connection Pooling**: Database connection management
- **Prepared Statements**: SQL execution optimization  
- **Session Management**: Memory-efficient session handling
- **Static Resource Caching**: CSS/JS minification
- **Database Indexing**: Query performance optimization

### Scalability Considerations
- **Horizontal Scaling**: Load balancer ready architecture
- **Database Sharding**: User-based data partitioning
- **Caching Strategy**: Redis integration ready
- **CDN Integration**: Static asset distribution

## 🎨 UI/UX Design

### Design Principles
- **Material Design**: Modern, clean aesthetic
- **Responsive Layout**: Mobile-first approach
- **Accessibility**: WCAG 2.1 compliance
- **User Experience**: Intuitive navigation flows
- **Professional Styling**: Corporate-grade appearance

### Visual Features
- **Gradient Backgrounds**: Modern color schemes
- **FontAwesome Icons**: Professional iconography
- **Smooth Animations**: CSS transitions
- **Interactive Elements**: Hover effects and feedback
- **Form Validation**: Real-time user guidance

## 📈 Future Enhancements

### Phase 2 Features
- [ ] **Real-time Notifications**: WebSocket integration
- [ ] **Mobile App**: React Native companion app
- [ ] **API Gateway**: RESTful API for third-party integration
- [ ] **Advanced Analytics**: Data visualization dashboards
- [ ] **Geolocation Services**: GPS-based complaint mapping

### Technical Improvements
- [ ] **Microservices Architecture**: Service decomposition
- [ ] **Docker Containerization**: Deployment standardization
- [ ] **CI/CD Pipeline**: Automated testing and deployment
- [ ] **Cloud Migration**: AWS/Azure deployment
- [ ] **Performance Monitoring**: APM tool integration

---

## 📞 Contact & Support

**Developer**: Java Full-Stack Developer  
**Project Type**: Enterprise Web Application  
**Purpose**: Urban infrastructure management and citizen services platform  

### 🏆 Skills Demonstrated

This project showcases proficiency in:
- **Enterprise Java Development** (Java EE, Servlets, JDBC)
- **Database Design & Management** (MySQL, SQL optimization)
- **Web Development** (JSP, HTML5, CSS3, JavaScript)
- **Security Implementation** (Authentication, authorization, data protection)
- **Software Architecture** (MVC, DAO patterns, layered architecture)
- **Development Best Practices** (Code organization, documentation, testing)
- **Project Management** (Version control, build automation, deployment)

---

### 📄 License

This is an enterprise web application for smart city infrastructure management.

**© 2025 Smart City Management System**