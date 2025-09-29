-- =====================================================
-- SMART CITY MANAGEMENT SYSTEM - COMPLETE DATABASE SETUP
-- Run this ENTIRE script in your vendor's database management tool
-- Compatible with PostgreSQL and MySQL
-- =====================================================

-- Step 1: Clean slate - Drop all existing tables (if any)
DROP TABLE IF EXISTS complaints CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Step 2: Create the users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'citizen',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 3: Create the complaints table
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

-- Step 4: Insert admin user
-- Username: admin, Password: admin123
INSERT INTO users (username, password, email, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin');

-- Step 5: Insert test citizen users
-- Username: citizen1, Password: test
INSERT INTO users (username, password, email, role) VALUES 
('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen');

-- Username: citizen2, Password: admin123  
INSERT INTO users (username, password, email, role) VALUES 
('citizen2', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'citizen2@example.com', 'citizen');

-- Username: testuser, Password: test
INSERT INTO users (username, password, email, role) VALUES 
('testuser', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'testuser@example.com', 'citizen');

-- Step 6: Insert sample complaints for testing
INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues near the intersection', 'Main Street & 1st Avenue', 'pending', 'high');

INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(2, 'Water Supply', 'Low water pressure', 'Experiencing very low water pressure in residential area during peak hours', 'Sector 15, Block A', 'in_progress', 'medium');

INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(3, 'Electricity', 'Street light not working', 'Street light has been out for 3 days creating safety concerns for pedestrians', 'Park Avenue', 'pending', 'medium');

INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(3, 'Sanitation', 'Garbage collection missed', 'Garbage has not been collected for over a week, causing hygiene issues', 'Green Valley Society', 'resolved', 'low');

INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(4, 'Public Transport', 'Bus stop needs repair', 'Bus stop shelter is damaged and needs immediate repair work', 'City Center Bus Stop', 'pending', 'low');

-- Step 7: Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_complaints_user_id ON complaints(user_id);
CREATE INDEX idx_complaints_status ON complaints(status);
CREATE INDEX idx_complaints_category ON complaints(category);
CREATE INDEX idx_complaints_created_at ON complaints(created_at);

-- Step 8: Verify the setup worked
SELECT 'DATABASE SETUP COMPLETE!' as message;

-- Show user count
SELECT 'USERS CREATED' as info, COUNT(*) as count FROM users;

-- Show complaint count  
SELECT 'COMPLAINTS CREATED' as info, COUNT(*) as count FROM complaints;

-- Display all users
SELECT 'ALL USERS:' as section;
SELECT id, username, email, role, created_at FROM users ORDER BY id;

-- Display all complaints with user names
SELECT 'ALL COMPLAINTS:' as section;
SELECT c.id, u.username as user, c.category, c.subject, c.status, c.priority, c.created_at 
FROM complaints c 
JOIN users u ON c.user_id = u.id 
ORDER BY c.created_at DESC;

-- =====================================================
-- LOGIN CREDENTIALS FOR TESTING:
-- =====================================================
-- Admin Login: 
--   Username: admin
--   Password: admin123
--
-- Citizen Logins:
--   Username: citizen1, Password: test
--   Username: citizen2, Password: admin123  
--   Username: testuser, Password: test
-- =====================================================