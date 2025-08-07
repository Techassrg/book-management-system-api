-- Migration: V2__Insert_sample_data.sql
-- Description: Insert sample book data for development and testing
-- Author: Book Management System
-- Date: 2025-08-07

-- Insert sample books data
INSERT INTO books (title, author, published_date) VALUES
('Kotlin Programming', 'Jane Smith', '2025-01-01'),
('Spring Boot Guide', 'John Doe', '2024-06-15'),
('Database Design', 'Alice Johnson', '2023-12-20'),
('Clean Code', 'Robert C. Martin', '2008-08-11'),
('Design Patterns', 'Erich Gamma', '1994-10-31'),
('Effective Java', 'Joshua Bloch', '2017-12-27'),
('The Pragmatic Programmer', 'Andrew Hunt', '1999-10-30'),
('Refactoring', 'Martin Fowler', '2018-11-19'),
('Test Driven Development', 'Kent Beck', '2002-11-18'),
('Domain-Driven Design', 'Eric Evans', '2003-08-22');
