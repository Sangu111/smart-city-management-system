-- PostgreSQL Schema for Smart City Management System
-- This script creates the database schema compatible with PostgreSQL

-- Drop tables if they exist (for fresh deployment)
DROP TABLE IF EXISTS complaints;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'citizen',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create complaints table
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

-- Insert default admin user (password: admin123)
INSERT INTO users (username, password, email, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin');

-- Insert sample citizen users (passwords: test, admin123)
INSERT INTO users (username, password, email, role) VALUES 
('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen'),
('citizen2', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'citizen2@example.com', 'citizen');

-- Insert sample complaints
INSERT INTO complaints (user_id, category, subject, description, location, status, priority) VALUES 
(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues near Main St and 1st Ave intersection', 'Main Street & 1st Avenue', 'pending', 'high'),
(2, 'Water Supply', 'Low water pressure', 'Experiencing very low water pressure in residential area', 'Sector 15, Block A', 'in_progress', 'medium'),
(3, 'Electricity', 'Street light not working', 'Street light has been out for 3 days creating safety concerns', 'Park Avenue', 'pending', 'medium'),
(3, 'Sanitation', 'Garbage collection missed', 'Garbage has not been collected for over a week', 'Green Valley Society', 'resolved', 'low');

-- Create indexes for better performance
CREATE INDEX idx_complaints_user_id ON complaints(user_id);
CREATE INDEX idx_complaints_status ON complaints(status);
CREATE INDEX idx_complaints_category ON complaints(category);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);