-- Migration: V3__Add_book_status_column.sql
-- Description: Add status column to books table for book lifecycle management
-- Author: Book Management System
-- Date: 2025-08-07

-- Add status column to books table
ALTER TABLE books 
ADD COLUMN status ENUM('AVAILABLE', 'BORROWED', 'RESERVED', 'MAINTENANCE') 
DEFAULT 'AVAILABLE' 
NOT NULL 
COMMENT 'Book availability status';

-- Add index for status queries
CREATE INDEX idx_status ON books(status);

-- Update existing books to have AVAILABLE status
UPDATE books SET status = 'AVAILABLE' WHERE status IS NULL;

-- Add comment for the new column
ALTER TABLE books MODIFY COLUMN status ENUM('AVAILABLE', 'BORROWED', 'RESERVED', 'MAINTENANCE') 
DEFAULT 'AVAILABLE' NOT NULL COMMENT 'Book availability status';
