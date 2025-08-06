-- Book Management System Database Schema
-- สคริปต์สำหรับสร้างฐานข้อมูลระบบจัดการหนังสือ

-- สร้างฐานข้อมูล
CREATE DATABASE IF NOT EXISTS book_management_system
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- ใช้ฐานข้อมูล
USE book_management_system;

-- สร้างตาราง books
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- สร้าง index สำหรับการค้นหาตามผู้เขียน (optimize query)
    INDEX idx_author (author),
    INDEX idx_published_date (published_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ข้อมูลตัวอย่าง (optional)
INSERT INTO books (title, author, published_date) VALUES
('Kotlin Programming', 'Jane Smith', '2025-01-01'),
('Spring Boot Guide', 'John Doe', '2024-06-15'),
('Database Design', 'Alice Johnson', '2023-12-20');

-- แสดงข้อมูลในตาราง
SELECT * FROM books; 