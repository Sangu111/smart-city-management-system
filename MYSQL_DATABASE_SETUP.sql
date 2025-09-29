-- =====================================================
-- SMART CITY MANAGEMENT SYSTEM - MYSQL VERSION
-- Use this if your vendor database is MySQL
-- =====================================================

-- Step 1: Clean slate
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS users;

-- Step 2: Create users table (MySQL version)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'citizen',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Step 3: Create complaints table (MySQL version)
CREATE TABLE complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(200),
    status VARCHAR(50) DEFAULT 'pending',
    priority VARCHAR(20) DEFAULT 'medium',
    photo_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Step 4: Insert users
INSERT INTO users (username, password, email, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin'),
('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen'),
('citizen2', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'citizen2@example.com', 'citizen'),
('testuser', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'testuser@example.com', 'citizen');

-- Step 5: Insert complaints
INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street & 1st Avenue', 'pending', 'high'),
(2, 'Water Supply', 'Low water pressure', 'Low water pressure in residential area', 'Sector 15, Block A', 'in_progress', 'medium'),
(3, 'Electricity', 'Street light not working', 'Street light out for 3 days', 'Park Avenue', 'pending', 'medium'),
(3, 'Sanitation', 'Garbage collection missed', 'Garbage not collected for over a week', 'Green Valley Society', 'resolved', 'low'),
(4, 'Public Transport', 'Bus stop needs repair', 'Bus stop shelter is damaged', 'City Center Bus Stop', 'pending', 'low');

-- Step 6: Create indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_complaints_user_id ON complaints(user_id);
CREATE INDEX idx_complaints_status ON complaints(status);

-- Step 7: Verify setup
SELECT 'Setup Complete!' as message;
SELECT COUNT(*) as user_count FROM users;
SELECT COUNT(*) as complaint_count FROM complaints;
SELECT * FROM users;
SELECT c.id, u.username, c.category, c.subject, c.status FROM complaints c JOIN users u ON c.user_id = u.id;