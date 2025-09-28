CREATE DATABASE IF NOT EXISTS smartcity;
USE smartcity;

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
    photo_url VARCHAR(255),
    status ENUM('Submitted', 'In Progress', 'Resolved', 'Rejected') DEFAULT 'Submitted',
    assigned_to VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Insert default admin user (username: admin, password: admin123)
INSERT INTO users (username, email, password_hash, role) 
VALUES ('admin', 'admin@smartcity.com', SHA2('admin123', 256), 'admin');

-- Insert sample citizen (username: citizen1, password: test123)
INSERT INTO users (username, email, password_hash, role) 
VALUES ('citizen1', 'citizen1@example.com', SHA2('test123', 256), 'citizen');

-- Insert sample complaints
INSERT INTO complaints (user_id, category, title, description, location, status) 
VALUES 
(2, 'Road', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street near City Hall', 'Submitted'),
(2, 'Water', 'Water leak in Park Avenue', 'Water pipe burst causing flooding', 'Park Avenue Block 5', 'In Progress');