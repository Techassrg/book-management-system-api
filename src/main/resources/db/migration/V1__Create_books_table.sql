-- Migration: V1__Create_books_table.sql
-- Description: Create books table with proper indexes and constraints
-- Author: Book Management System
-- Date: 2025-08-07

-- Create books table
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Create indexes for query optimization
    INDEX idx_author (author),
    INDEX idx_published_date (published_date),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add comments for documentation
ALTER TABLE books COMMENT = 'Books table for storing book information';
ALTER TABLE books MODIFY COLUMN title VARCHAR(255) NOT NULL COMMENT 'Book title';
ALTER TABLE books MODIFY COLUMN author VARCHAR(255) NOT NULL COMMENT 'Book author';
ALTER TABLE books MODIFY COLUMN published_date DATE NOT NULL COMMENT 'Book publication date';
