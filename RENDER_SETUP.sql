-- =====================================================
-- Smart City Database Setup for Render PostgreSQL
-- Database ID: dpg-d3d451ggjchc739mojhg-a
-- =====================================================

-- Connect to your database using Render's web console or psql:
-- psql postgres://dpg-d3d451ggjchc739mojhg-a.singapore-postgres.render.com:5432/smartcity_wb0d

-- Step 1: Check current database status
SELECT current_database(), current_user, version();

-- Step 2: Drop existing tables if they exist
DROP TABLE IF EXISTS complaints CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Step 3: Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'citizen',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 4: Create complaints table
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

-- Step 5: Insert admin user (password: admin123)
INSERT INTO users (username, password, email, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin');

-- Step 6: Insert citizen users (password: test)
INSERT INTO users (username, password, email, role) VALUES 
('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen'),
('citizen2', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'citizen2@example.com', 'citizen');

-- Step 7: Insert sample complaints
INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street & 1st Avenue', 'pending', 'high'),
(2, 'Water Supply', 'Low water pressure', 'Water pressure is very low in residential area', 'Sector 15, Block A', 'in_progress', 'medium'),
(3, 'Electricity', 'Street light not working', 'Street light out for 3 days, safety concern', 'Park Avenue', 'pending', 'medium'),
(3, 'Sanitation', 'Garbage collection missed', 'Garbage not collected for over a week', 'Green Valley Society', 'resolved', 'low');

-- Step 8: Create indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_complaints_user_id ON complaints(user_id);
CREATE INDEX idx_complaints_status ON complaints(status);

-- Step 9: Verify setup
SELECT 'Setup complete!' as message;
SELECT 'Users created:', COUNT(*) FROM users;
SELECT 'Complaints created:', COUNT(*) FROM complaints;

-- Step 10: Display all data
SELECT 'All Users:' as info;
SELECT id, username, email, role, created_at FROM users ORDER BY id;

SELECT 'All Complaints:' as info;
SELECT c.id, u.username, c.category, c.subject, c.status, c.created_at 
FROM complaints c 
JOIN users u ON c.user_id = u.id 
ORDER BY c.id;