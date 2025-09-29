# How to Setup Database with Your Vendor

## Step-by-Step Instructions:

### Option 1: Use the Complete SQL File
1. **Open** `VENDOR_DATABASE_SETUP.sql` file in this project
2. **Copy** the entire SQL script
3. **Login** to your vendor's database management panel (phpMyAdmin, pgAdmin, etc.)
4. **Paste** the SQL script and **Execute** it
5. **Done!** Your database is ready

### Option 2: Use Individual Queries

#### 1. Create Tables First:
```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) DEFAULT 'citizen',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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
```

#### 2. Insert Test Users:
```sql
INSERT INTO users (username, password, email, role) VALUES 
('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin'),
('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen');
```

### Option 3: Use Web Interface
Visit your deployed app and use these URLs:
- **View Current Data:** `https://smart-city-management-system-nnev.onrender.com/database-manager`
- **Get SQL Queries:** Copy from the database manager page

## Test Your Setup:
After running the SQL, try logging in with:
- **Admin:** username = `admin`, password = `admin123`
- **Citizen:** username = `citizen1`, password = `test`

## Update Your App Configuration:
Update your `db.properties` with your vendor's database connection details.