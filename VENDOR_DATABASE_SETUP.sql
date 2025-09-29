# Smart City Management System - Database Setup for Vendor

## Complete SQL Script for PostgreSQL/MySQL

Copy and paste this entire script into your vendor's database management tool:

```sql
-- =====================================================
-- Smart City Management System Database Setup
-- Compatible with PostgreSQL and MySQL
-- =====================================================

-- Step 1: Drop existing tables (if any)
DROP TABLE IF EXISTS complaints CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Step 2: Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'citizen',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 3: Create complaints table
CREATE TABLE complaints (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    category VARCHAR(50) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(200),
    status VARCHAR(50) DEFAULT 'pending',
    priority VARCHAR(20) DEFAULT 'medium',
    photo_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Step 4: Insert default users
-- Admin user (username: admin, password: admin123)
INSERT INTO users (username, password, email, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin');

-- Citizen user (username: citizen1, password: test)
INSERT INTO users (username, password, email, role) VALUES 
('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen');

-- Additional citizen users for testing
INSERT INTO users (username, password, email, role) VALUES 
('citizen2', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'citizen2@example.com', 'citizen'),
('testuser', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'testuser@example.com', 'citizen');

-- Step 5: Insert sample complaints
INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues near intersection', 'Main Street & 1st Avenue', 'pending', 'high'),
(2, 'Water Supply', 'Low water pressure', 'Experiencing very low water pressure in residential area', 'Sector 15, Block A', 'in_progress', 'medium'),
(3, 'Electricity', 'Street light not working', 'Street light has been out for 3 days creating safety concerns', 'Park Avenue', 'pending', 'medium'),
(3, 'Sanitation', 'Garbage collection missed', 'Garbage has not been collected for over a week', 'Green Valley Society', 'resolved', 'low');

-- Step 6: Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_complaints_user_id ON complaints(user_id);
CREATE INDEX idx_complaints_status ON complaints(status);
CREATE INDEX idx_complaints_category ON complaints(category);

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Check if tables were created successfully
SELECT 'Users table created' AS status, COUNT(*) AS user_count FROM users;
SELECT 'Complaints table created' AS status, COUNT(*) AS complaint_count FROM complaints;

-- Display all users
SELECT id, username, email, role, created_at FROM users ORDER BY id;

-- Display all complaints
SELECT c.id, u.username, c.category, c.subject, c.status, c.created_at 
FROM complaints c 
LEFT JOIN users u ON c.user_id = u.id 
ORDER BY c.id;
```

## Login Credentials for Testing:

1. **Admin Access:**
   - Username: `admin`
   - Password: `admin123`

2. **Citizen Access:**
   - Username: `citizen1`
   - Password: `test`

3. **Additional Test Users:**
   - Username: `citizen2` / Password: `admin123`
   - Username: `testuser` / Password: `test`

## Database Configuration for Your Application:

If you're connecting from your Java application, update your `db.properties` file with your vendor's database details:

```properties
# PostgreSQL Configuration
db.url=jdbc:postgresql://YOUR_VENDOR_HOST:5432/YOUR_DATABASE_NAME
db.username=YOUR_USERNAME
db.password=YOUR_PASSWORD
db.driver=org.postgresql.Driver

# OR for MySQL
# db.url=jdbc:mysql://YOUR_VENDOR_HOST:3306/YOUR_DATABASE_NAME
# db.username=YOUR_USERNAME
# db.password=YOUR_PASSWORD
# db.driver=com.mysql.cj.jdbc.Driver
```

## Important Notes:

1. **Password Hashing:** The passwords are pre-hashed using SHA-256
   - `admin123` → `240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9`
   - `test` → `9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08`

2. **Database Compatibility:** This script works with both PostgreSQL and MySQL

3. **Clean Setup:** The script will drop existing tables and recreate them

4. **Sample Data:** Includes users and complaints for immediate testing