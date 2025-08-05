package com.asmt.Book_Management_System.repository

import com.asmt.Book_Management_System.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BookRepository : JpaRepository<Book, Long> {
    
    /**
     * Find books by author with optimized query
     * This method will use the index on author column for better performance
     */
    @Query("SELECT b FROM Book b WHERE b.author = :author ORDER BY b.publishedDate DESC")
    fun findByAuthor(@Param("author") author: String): List<Book>
    
    /**
     * Find books by author containing (case-insensitive search)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')) ORDER BY b.publishedDate DESC")
    fun findByAuthorContainingIgnoreCase(@Param("author") author: String): List<Book>
}
