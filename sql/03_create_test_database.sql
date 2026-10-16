-- Create a separate database for automated testing.
-- This script does not modify the original application database.

CREATE DATABASE IF NOT EXISTS student_expense_tracker_test;

USE student_expense_tracker_test;

CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS expenses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    category_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    expense_date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS budgets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE (user_id, month, year)
);

INSERT IGNORE INTO categories (name) VALUES
('Food'),
('Travel'),
('Education'),
('Shopping'),
('Entertainment'),
('Bills'),
('Health'),
('Other');

-- Verify that the test database is selected.
SELECT DATABASE();

SHOW TABLES;

SELECT * FROM categories;