package com.asmt.bookmanagementsystem.repository

import com.asmt.bookmanagementsystem.entity.Book
import com.asmt.bookmanagementsystem.entity.BookStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BookRepository : JpaRepository<Book, Long> {
    
    /**
     * Find books by author with optimized query
     * This method will use the index on author column 
     */
    @Query("SELECT b FROM Book b WHERE b.author = :author ORDER BY b.publishedDate DESC")
    fun findByAuthor(@Param("author") author: String): List<Book>
    
    /**
     * Find books by author containing (case-insensitive search)
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')) ORDER BY b.publishedDate DESC")
    fun findByAuthorContainingIgnoreCase(@Param("author") author: String): List<Book>
    
    /**
     * Find books by status with optimized query
     * This method will use the index on status column
     */
    @Query("SELECT b FROM Book b WHERE b.status = :status ORDER BY b.createdAt DESC")
    fun findByStatus(@Param("status") status: BookStatus): List<Book>
    
    /**
     * Find books by author and status
     */
    @Query("SELECT b FROM Book b WHERE b.author = :author AND b.status = :status ORDER BY b.publishedDate DESC")
    fun findByAuthorAndStatus(@Param("author") author: String, @Param("status") status: BookStatus): List<Book>
}
