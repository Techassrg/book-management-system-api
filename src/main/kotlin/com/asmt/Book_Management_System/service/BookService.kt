package com.asmt.Book_Management_System.service

import com.asmt.Book_Management_System.dto.BookRequest
import com.asmt.Book_Management_System.entity.Book
import com.asmt.Book_Management_System.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class BookService(private val bookRepository: BookRepository) {

    /**
     * Get books by author with optimized query
     * Uses index on author column for better performance
     */
    fun getBooksByAuthor(author: String): List<Book> {
        if (author.isBlank()) {
            throw IllegalArgumentException("Author name cannot be empty")
        }
        return bookRepository.findByAuthor(author.trim())
    }

    /**
     * Save a new book with validation
     * Converts Buddhist calendar to Gregorian calendar
     */
    fun saveBook(request: BookRequest): Book {
        val date = request.toGregorianDate()
        val year = date.year
        
        // Validate year range
        if (year <= 1000) {
            throw IllegalArgumentException("Published year must be greater than 1000")
        }
        if (year > LocalDate.now().year) {
            throw IllegalArgumentException("Published year cannot be in the future")
        }
        
        val book = Book(
            title = request.title.trim(),
            author = request.author.trim(),
            publishedDate = date
        )
        
        return bookRepository.save(book)
    }
}
